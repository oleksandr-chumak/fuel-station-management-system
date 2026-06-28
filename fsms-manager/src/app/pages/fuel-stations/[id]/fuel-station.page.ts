import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, OnDestroy, OnInit, Signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { SkeletonModule } from 'primeng/skeleton';
import { TabsModule } from 'primeng/tabs';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelStation } from 'fsms-web-api';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { GetFuelStationByIdHandler } from '../../../modules/fuel-station/handlers/get-fuel-station-by-id.handler';
import { FuelStationStore } from '../../../modules/fuel-station/fuel-station-store';
import { FuelStationEventHandler } from '../../../modules/fuel-station/fuel-station-event-handler';
import { catchError, EMPTY } from 'rxjs';

@Component({
  selector: 'app-fuel-station-page',
  imports: [RouterModule, TabsModule, CommonModule, SkeletonModule, TranslatePipe],
  templateUrl: './fuel-station.page.html'
})
export class FuelStationPage implements OnInit, OnDestroy {
  private paramsStationId: string = '';
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly destroyRef = inject(DestroyRef);

  private readonly getFuelStationByIdHandler = inject(GetFuelStationByIdHandler);
  private readonly fuelStationEventHandler = inject(FuelStationEventHandler);
  private readonly store = inject(FuelStationStore);

  private readonly messageService = inject(MessageService);
  private readonly translate = inject(TranslateService);

  protected readonly initialRoute = this.router.url;
  protected readonly fuelStation = toSignal(this.store.fuelStation$, { initialValue: null });

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.paramsStationId = params["id"];
      const stationId = Number(this.paramsStationId);

      if (Number.isNaN(stationId)) {
        this.messageService.add({
          severity: "error",
          summary: this.translate.instant('fuelStations.errors.unableToParseId'),
          detail: this.translate.instant('fuelStations.errors.unableToParseIdDetail') + ' ' + params["id"]
        });
        console.log("redirect");
        this.router.navigate(["/"]);
        return;
      }

      this.getFuelStationByIdHandler
        .handle({ fuelStationId: stationId })
        .pipe(
          catchError(() => {
            this.router.navigate(["/"]);
            return EMPTY;
          })
        )
        .subscribe();

      this.handleFuelStationEvents(stationId);
    });
  }

  ngOnDestroy(): void {
    this.store.reset();
  }

  private handleFuelStationEvents(fuelStationId: number): void {
    this.fuelStationEventHandler.start(fuelStationId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }

  get tabs() {
    return [
      {
        label: this.translate.instant('fuelStations.info'),
        icon: 'pi pi-info-circle',
        route: `/fuel-station/${this.paramsStationId}/info`
      },
      {
        label: this.translate.instant('header.managers'),
        icon: 'pi pi-users',
        route: `/fuel-station/${this.paramsStationId}/managers`
      },
      {
        label: this.translate.instant('fuelStations.fuelOrders'),
        icon: 'pi pi-shopping-cart',
        route: `/fuel-station/${this.paramsStationId}/fuel-orders`
      },
      {
        label: this.translate.instant('fuelStations.fuelTanks'),
        icon: "pi pi-box",
        route: `/fuel-station/${this.paramsStationId}/fuel-tanks`
      },
      {
        label: this.translate.instant('fuelStations.fuelPrices'),
        icon: "pi pi-dollar",
        route: `/fuel-station/${this.paramsStationId}/fuel-prices`
      }
    ];
  }
}
