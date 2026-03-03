import { BehaviorSubject, catchError, EMPTY, finalize, map, take, tap } from "rxjs";
import { inject, Injectable, OnDestroy } from "@angular/core";
import { DomainEventResponse, FuelStationRestClient } from "fsms-web-api";
import { FuelStationStore } from "./fuel-station-store";

@Injectable({ providedIn: "root" })
export class FuelStationEventsStore implements OnDestroy {
  private readonly fuelStationRestClient = inject(FuelStationRestClient);
  private readonly fuelStationStore = inject(FuelStationStore);

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
        }),
        catchError((error) => {
          console.error("Failed to fetch more events:", error);
          return EMPTY;
        }),
        finalize(() => this._loading.next(false))
      )
      .subscribe()
  }

  // TODO
  // incrementTotalEvents(): void {
  //   const current = this._totalEvents.getValue();
  //   if (current !== null) this._totalEvents.next(current + 1);
  // }
  // isFirstPage() {
  //   return this._currentPage.getValue() === 1;
  // }

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