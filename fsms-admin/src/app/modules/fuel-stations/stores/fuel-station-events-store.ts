import { BehaviorSubject, catchError, EMPTY, finalize, map, take, tap } from "rxjs";
import { inject, Injectable, OnDestroy } from "@angular/core";
import { DomainEventResponse, FuelStationRestClient } from "fsms-web-api";
import { FuelStationStore } from "./fuel-station-store";

@Injectable({ providedIn: "root" })
export class FuelStationEventsStore implements OnDestroy {
  private readonly fuelStationRestClient = inject(FuelStationRestClient);
  private readonly fuelStationStore = inject(FuelStationStore);

  private _isEventsLoaded = false;
  private readonly _currentPageEvents = new BehaviorSubject<DomainEventResponse[]>([]);
  private readonly _prefetchedEvents = new BehaviorSubject<DomainEventResponse[]>([]);
  private readonly _currentPage = new BehaviorSubject<number>(1);
  private readonly _totalEvents = new BehaviorSubject<number>(0);
  private readonly _loading = new BehaviorSubject<boolean>(false);

  readonly currentPageEvents$ = this._currentPageEvents.asObservable();
  readonly currentPage$ = this._currentPage.asObservable();
  readonly totalEvents$ = this._totalEvents.asObservable();
  readonly loading$ = this._loading.asObservable();

  readonly paginationRange = 5;
  readonly eventsPerPage = 10;

  reset(): void {
    this._currentPageEvents.next([]);
    this._prefetchedEvents.next([]);
    this._currentPage.next(1);
    this._totalEvents.next(0);
    this._loading.next(false);
    this._isEventsLoaded = false;
  }

  ngOnDestroy(): void {
    this._currentPageEvents.complete();
    this._prefetchedEvents.complete();
    this._currentPage.complete();
    this._totalEvents.complete();
    this._loading.complete();
  }

  changePage(newPage: number): void {
    if(this._currentPage.getValue() === newPage) {
      return;
    }

    const events = this.getEventsForPage(newPage);
    const diff = newPage - this._currentPage.getValue();
    this._currentPage.next(newPage) 

    if (diff < 0) {
      this._currentPageEvents.next(events)
      return;
    }
    
    const prefetched = this._prefetchedEvents.getValue();
    if(prefetched.length === this._totalEvents.getValue()) {
      this._currentPageEvents.next(events)
      return;
    }

    const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
    const limit = diff * this.eventsPerPage;
    const occurredAfter = prefetched[prefetched.length - 1].occurredAt;

    this._loading.next(true);
    this._currentPageEvents.next([]);

    this.fuelStationRestClient.getFuelStationEvents(fuelStationId, limit, occurredAfter)
      .pipe(
        take(1),
        tap((page) => {
          this._prefetchedEvents.next([
            ...this._prefetchedEvents.getValue(),
            ...page.content
          ]);

          this._currentPageEvents.next(events)
        }),
        catchError((error) => {
          console.error("Failed to fetch more events:", error);
          return EMPTY;
        }),
        finalize(() => this._loading.next(false))
      )
      .subscribe()
  }

  fetchEvents() {
    if(this._isEventsLoaded) {
      return;
    }

    const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
    const limit = this.eventsPerPage * this.paginationRange;

    this._loading.next(true);

    this.fuelStationRestClient.getFuelStationEvents(fuelStationId, limit)
      .pipe(
        take(1),
        tap((page) => {
          this._totalEvents.next(page.totalElements);
          this._prefetchedEvents.next(page.content);
          this._currentPageEvents.next(page.content.slice(0, this.eventsPerPage));
          this._isEventsLoaded = true;
        }),
        catchError((error) => {
          console.error("Failed to fetch more events:", error);
          return EMPTY;
        }),
        finalize(() => this._loading.next(false))
      )
      .subscribe()
  }

  prependEventByOccurredAt(occurredAt: string): void {
    console.log({occurredAt, isEventsLoaded: this._isEventsLoaded});
    if(!this._isEventsLoaded) {
      return;
    }

    const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
    const occurredAfter = this.addOneMicroSecond(occurredAt);

    this.fuelStationRestClient
      .getFuelStationEvents(fuelStationId, 1, occurredAfter)
      .pipe(
        take(1),
        tap((page) => {
          if (page.content.length !== 1) {
            console.error("[FuelStationEventsStore] Failed to fetch event by occurred at: ", occurredAt);
            return;
          };

          this._prefetchedEvents.next([page.content[0], ...this._prefetchedEvents.getValue()]);
          this._totalEvents.next(this._totalEvents.getValue() + 1);
          this._currentPageEvents.next(this.getEventsForPage(this._currentPage.getValue()));
        }),
        catchError((error) => {
          console.error("[FuelStationEventsStore] Failed to fetch event by occurred at: ", occurredAt, error);
          return EMPTY;
        })
      )
      .subscribe();
  }

  private addOneMicroSecond(timestamp: string) {
    const [datePart, fractional] = timestamp.split(".");
    const microseconds = fractional.slice(0, 6);
    const tz = fractional.slice(6); 

    const date = new Date(datePart + "Z");
    date.setUTCMilliseconds(date.getUTCMilliseconds());

    const newDatePart = date.toISOString().split(".")[0];
    const newMicro = String(Number(microseconds) + 1).padStart(6, "0");

    return `${newDatePart}.${newMicro}${tz}`;
  }

  private getEventsForPage(page: number): DomainEventResponse[] {
    if (!this.hasPrefetchedPage(page)) {
      throw new Error("Unable to retrieve events because events are not prefetched");
    }

    const start = (page - 1) * this.eventsPerPage;
    const end = page * this.eventsPerPage;
    return this._prefetchedEvents.getValue().slice(start, end);
  }

  private hasPrefetchedPage(page: number) {
    return page <= Math.ceil(this._prefetchedEvents.getValue().length / this.eventsPerPage);
  }
}