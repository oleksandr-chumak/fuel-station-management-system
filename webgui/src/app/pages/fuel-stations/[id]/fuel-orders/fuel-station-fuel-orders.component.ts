import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { Observable } from 'rxjs';

import { FuelGrade } from '~common/api/fuel-grade.enum';
import { FuelOrderStatus } from '~fuel-order/api/models/fuel-order-status.enum';
import { CreateFuelOrderDialogComponent } from '~fuel-order/application/components/create-fuel-order-dialog/create-fuel-order-dialog.component';
import { FuelStationContext } from '~fuel-station/application/models/fuel-station-context.model';
import { ManagerFuelStationContextLoadingEvent } from '~fuel-station/application/models/manager-fuel-station-context-loading-event.enum';
import { ManagerFuelStationContextService } from '~fuel-station/application/services/manager-fuel-station-context.service';

@Component({
  selector: 'app-fuel-station-fuel-orders',
  imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule, CreateFuelOrderDialogComponent],
  templateUrl: './fuel-station-fuel-orders.component.html'
})
export class FuelStationFuelOrdersComponent implements OnInit {
  private messageService: MessageService = inject(MessageService);
  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);

  loading = false;
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);
  
  ngOnInit(): void {
    this.getFuelOrders();
    this.ctxService.loadingEvents$.subscribe((event) => {
      if(event?.type === ManagerFuelStationContextLoadingEvent.GET_FUEL_ORDERS) {
        this.loading = event.value;
      }
    });
  }

  getSeverity(fuelOrderStatus: FuelOrderStatus): 'success' | 'info' | 'danger' | undefined {
    switch(fuelOrderStatus) {
    case FuelOrderStatus.Confirmed:
      return 'success';
    case FuelOrderStatus.Pending:
      return 'info';
    case FuelOrderStatus.Rejected:
      return 'danger';
    default:
      return undefined;
    }
  }
  
  getValue(fuelOrderStatus: FuelOrderStatus) {
    return FuelOrderStatus[fuelOrderStatus];
  }

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

  get ctx$(): Observable<FuelStationContext | null>  {
    return this.ctxService.getContext();
  }

  private getFuelOrders() {
    this.ctxService.getFuelOrders()
      .subscribe({
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while fetching fuel orders'});
        }
      }); 
  }
}
