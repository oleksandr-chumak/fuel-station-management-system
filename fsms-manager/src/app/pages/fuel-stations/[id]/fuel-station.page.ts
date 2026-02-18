import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit, Signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { SkeletonModule } from 'primeng/skeleton';
import { TabsModule } from 'primeng/tabs';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelStation } from 'fsms-web-api';
import { GetFuelStationByIdHandler } from '../../../modules/fuel-station/handlers/get-fuel-station-by-id.handler';
import { FuelStationStore } from '../../../modules/fuel-station/fuel-station-store';
import { catchError, EMPTY } from 'rxjs';

@Component({
  selector: 'app-fuel-station-page',
  imports: [RouterModule, TabsModule, CommonModule, SkeletonModule],
  templateUrl: './fuel-station.page.html'
})
export class FuelStationPage implements OnInit, OnDestroy {
  private paramsStationId: string = '';
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  private readonly getFuelStationByIdHandler = inject(GetFuelStationByIdHandler);
  private readonly store = inject(FuelStationStore);

  private readonly messageService = inject(MessageService);

  fuelStation: Signal<FuelStation | null> = toSignal(this.store.fuelStation$, { initialValue: null });

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.paramsStationId = params["id"];
      const stationId = Number(this.paramsStationId);

      if (Number.isNaN(stationId)) {
        this.messageService.add({ severity: "error", summary: "Unable to parse id", detail: "Unable to parse fuel station id: " + params["id"] });
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
    });
  }

  ngOnDestroy(): void {
    this.store.reset();
  }

  get tabs() {
    return [
      {
        label: 'Info',
        icon: 'pi pi-info-circle',
        route: `/fuel-station/${this.paramsStationId}/info`
      },
      {
        label: 'Managers',
        icon: 'pi pi-users',
        route: `/fuel-station/${this.paramsStationId}/managers`
      },
      {
        label: 'Fuel Orders',
        icon: 'pi pi-shopping-cart',
        route: `/fuel-station/${this.paramsStationId}/fuel-orders`
      },
      {
        label: "Fuel Tanks",
        icon: "pi pi-box",
        route: `/fuel-station/${this.paramsStationId}/fuel-tanks`
      },
      {
        label: "Fuel Prices",
        icon: "pi pi-dollar",
        route: `/fuel-station/${this.paramsStationId}/fuel-prices`
      }
    ];
  }
}
