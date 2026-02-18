import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TabsModule } from 'primeng/tabs';
import { MessageService } from 'primeng/api';
import { SkeletonModule } from 'primeng/skeleton';
import { GetFuelStationByIdHandler } from '../../../modules/fuel-stations/handlers/get-fuel-station-by-id-handler';
import { catchError, EMPTY, Subscription, tap } from 'rxjs';
import { FuelStationStore } from '../../../modules/fuel-stations/fuel-station-store';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelStationEventHandler } from '../../../modules/fuel-stations/fuel-station-event-handler';

@Component({
  selector: 'app-fuel-station-page',
  imports: [RouterModule, TabsModule, CommonModule, SkeletonModule],
  templateUrl: './fuel-station.page.html'
})
export class FuelStationPage implements OnInit, OnDestroy {
  private eventSubscription: Subscription | null = null;
  private paramsFuelStationId = "";
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute)

  private readonly getFuelStationByIdHandler = inject(GetFuelStationByIdHandler);
  private readonly fuelStationEventHandler = inject(FuelStationEventHandler);
  private readonly fuelStationStore = inject(FuelStationStore);

  private readonly messageService: MessageService = inject(MessageService);

  protected readonly fuelStation = toSignal(this.fuelStationStore.fuelStation$, { initialValue: null });
  protected readonly loading = toSignal(this.getFuelStationByIdHandler.loading$, { initialValue: false });

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.paramsFuelStationId = params["id"];
      const fuelStationId = Number(this.paramsFuelStationId);

      if(Number.isNaN(fuelStationId)) {
        this.messageService.add({severity: "error", summary: "Unable to parse id", detail: "Unable to parse fuel station id: " + params["id"]})
        this.router.navigate(["/admin"]);
        return;
      }

      this.getFuelStationByIdHandler.handle({ fuelStationId })
        .pipe(
          catchError(() => { 
            this.router.navigate(["/admin"]); 
            return EMPTY; 
          }),
          tap((fuelStation) => {
            this.eventSubscription = this.fuelStationEventHandler
              .start(fuelStation.fuelStationId)
              .subscribe();
          })
        )
        .subscribe()
    });
  }

  ngOnDestroy(): void {
    this.eventSubscription?.unsubscribe();
    this.fuelStationStore.reset();
  }

  get tabs() {
    return (
      [
        {
          label: "Info",
          icon: "pi pi-info-circle",
          route: `/fuel-stations/${this.paramsFuelStationId}/info`
        },
        {
          label: "Managers",
          icon: "pi pi-users",
          route: `/fuel-stations/${this.paramsFuelStationId}/managers`
        },
        {
          label: "Fuel Orders",
          icon: "pi pi-list",
          route: `/fuel-stations/${this.paramsFuelStationId}/fuel-orders`
        },
        {
          label: "Fuel Tanks", 
          icon: "pi pi-box",
          route: `/fuel-stations/${this.paramsFuelStationId}/fuel-tanks`
        },
        {
          label: "Fuel Prices", 
          icon: "pi pi-dollar",
          route: `/fuel-stations/${this.paramsFuelStationId}/fuel-prices`
        }
      ]
    )
  }

}
