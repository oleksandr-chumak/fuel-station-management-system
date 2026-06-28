import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router, RouterModule } from '@angular/router';
import { TabsModule } from 'primeng/tabs';
import { MessageService } from 'primeng/api';
import { SkeletonModule } from 'primeng/skeleton';
import { GetFuelStationByIdHandler } from '../../../modules/fuel-stations/handlers/get-fuel-station-by-id-handler';
import { catchError, EMPTY } from 'rxjs';
import { FuelStationStore } from '../../../modules/fuel-stations/stores/fuel-station-store';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { FuelStationEventHandler } from '../../../modules/fuel-stations/fuel-station-event-handler';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-fuel-station-page',
  imports: [RouterModule, TabsModule, CommonModule, SkeletonModule, TranslatePipe],
  templateUrl: './fuel-station.page.html'
})
export class FuelStationPage implements OnInit, OnDestroy {
  private readonly translate = inject(TranslateService);
  private readonly destroyRef = inject(DestroyRef)
  private paramsFuelStationId = "";
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute)

  private readonly getFuelStationByIdHandler = inject(GetFuelStationByIdHandler);
  private readonly fuelStationEventHandler = inject(FuelStationEventHandler);
  private readonly fuelStationStore = inject(FuelStationStore);

  private readonly messageService: MessageService = inject(MessageService);

  protected readonly initialRoute = this.router.url;
  protected readonly fuelStation = toSignal(this.fuelStationStore.fuelStation$, { initialValue: null });
  protected readonly loading = toSignal(this.getFuelStationByIdHandler.loading$, { initialValue: false });

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const fuelStationId = this.getFuelStationIdFromParams(params);
      if(fuelStationId === null) {
        return;
      };

      this.fetchFuelStation(fuelStationId);
      this.handleFuelStationEvents(fuelStationId);
    });
  }

  ngOnDestroy(): void {
    this.fuelStationStore.reset();
  }

  private getFuelStationIdFromParams(params: Params): number | null {
      this.paramsFuelStationId = params["id"];
      const fuelStationId = Number(this.paramsFuelStationId);
      if (Number.isNaN(fuelStationId)) {
        this.messageService.add({
          severity: "error",
          summary: this.translate.instant('fuelStations.errors.unableToParseId'),
          detail: this.translate.instant('fuelStations.errors.unableToParseIdDetail') + " " + params["id"]
        });
        this.router.navigate(["/"]);
        return null;
      }
      return fuelStationId
  }

  private handleFuelStationEvents(fuelStationId: number) {
    this.fuelStationEventHandler
      .start(fuelStationId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }

  private fetchFuelStation(fuelStationId: number) {
    this.getFuelStationByIdHandler.handle({ fuelStationId })
      .pipe(
        catchError(() => {
          this.router.navigate(["/"]);
          return EMPTY;
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe()
  }

  get tabs() {
    return (
      [
        {
          label: this.translate.instant('fuelStations.info'),
          icon: "pi pi-info-circle",
          route: `/fuel-stations/${this.paramsFuelStationId}/info`
        },
        {
          label: this.translate.instant('header.managers'),
          icon: "pi pi-users",
          route: `/fuel-stations/${this.paramsFuelStationId}/managers`
        },
        {
          label: this.translate.instant('fuelStations.fuelOrders'),
          icon: "pi pi-list",
          route: `/fuel-stations/${this.paramsFuelStationId}/fuel-orders`
        },
        {
          label: this.translate.instant('fuelStations.fuelTanks'),
          icon: "pi pi-box",
          route: `/fuel-stations/${this.paramsFuelStationId}/fuel-tanks`
        },
        {
          label: this.translate.instant('fuelStations.fuelPrices'),
          icon: "pi pi-dollar",
          route: `/fuel-stations/${this.paramsFuelStationId}/fuel-prices`
        },
        {
          label: this.translate.instant('fuelStations.finance'),
          icon: "pi pi-receipt",
          route: `/fuel-stations/${this.paramsFuelStationId}/fuel-purchases`
        },
        {
          label: this.translate.instant('fuelStations.events'),
          icon: "pi pi-history",
          route: `/fuel-stations/${this.paramsFuelStationId}/events`
        }
      ]
    )
  }

}
