import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { SkeletonModule } from 'primeng/skeleton';
import { TabsModule } from 'primeng/tabs';
import ManagerFuelStationContextService from '../../modules/fuel-station/services/manager-fuel-station-context.service';

@Component({
  selector: 'app-fuel-station',
  imports: [RouterModule, TabsModule, CommonModule, SkeletonModule],
  templateUrl: './fuel-station.component.html'
})
export class FuelStationComponent implements OnInit, OnDestroy {
  private paramsStationId = '';

  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);
  private messageService: MessageService = inject(MessageService);
  private router: Router = inject(Router);
  private route: ActivatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.paramsStationId = params["id"];
      const stationId = Number(this.paramsStationId);

      if(Number.isNaN(stationId)) {
        this.messageService.add({severity: "error", summary: "Unable to parse id", detail: "Unable to parse fuel station id: " + params["id"]})
        this.router.navigate(["/admin"]);
        return;
      }

      this.ctxService.getFuelStation(stationId)
        .subscribe({
          error: () => {
            this.messageService.add({severity: "error", summary: "Not found", detail: "Fuel station with id: " + stationId + " doesn't exist"})
            this.router.navigate(["/admin"]);
          }
        })
    });
  }

  ngOnDestroy(): void {
    this.ctxService.resetContext();
  }

  get tabs() {
    return (
      [
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
      ]
    )
  }

  get ctx$() {
    return this.ctxService.getContext();
  }
}
