import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TabsModule } from 'primeng/tabs';
import AdminFuelStationContextService from '../../../../modules/fuel-station/services/admin-fuel-station-context.service';
import { MessageService } from 'primeng/api';
import { SkeletonModule } from 'primeng/skeleton';

@Component({
  selector: 'app-admin-fuel-station',
  imports: [RouterModule, TabsModule, CommonModule, SkeletonModule],
  templateUrl: './admin-fuel-station.component.html'
})
export class AdminFuelStationComponent implements OnInit, OnDestroy {
  private paramsStationId = '';
  private router: Router = inject(Router);
  private route: ActivatedRoute = inject(ActivatedRoute)
  private messageService: MessageService = inject(MessageService);
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService)

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.paramsStationId = params['id'];
      const stationId = Number(this.paramsStationId);

      if(Number.isNaN(stationId)) {
        this.messageService.add({severity: 'error', summary: 'Unable to parse id', detail: 'Unable to parse fuel station id: ' + params['id']})
        this.router.navigate(['/admin']);
        return;
      }

      this.ctxService.getFuelStation(stationId)
        .subscribe({
          error: () => {
            this.messageService.add({severity: 'error', summary: 'Not found', detail: 'Fuel station with id: ' + stationId + " doesn't exist"})
            this.router.navigate(['/admin']);
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
          route: `/admin/fuel-station/${this.paramsStationId}/info`
        },
        {
          label: 'Managers',
          icon: 'pi pi-users',
          route: `/admin/fuel-station/${this.paramsStationId}/managers`
        },
        {
          label: 'Fuel Orders',
          icon: 'pi pi-list',
          route: `/admin/fuel-station/${this.paramsStationId}/fuel-orders`
        },
        {
          label: 'Fuel Tanks', 
          icon: 'pi pi-box',
          route: `/admin/fuel-station/${this.paramsStationId}/fuel-tanks`
        },
        {
          label: 'Fuel Prices', 
          icon: 'pi pi-dollar',
          route: `/admin/fuel-station/${this.paramsStationId}/fuel-prices`
        }
      ]
    )
  }

  get ctx$() {
    return this.ctxService.getContext();
  }
}
