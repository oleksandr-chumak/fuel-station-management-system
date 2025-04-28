import { Component, inject, OnInit } from '@angular/core';
import ManagerFuelStationContextService from '../../../modules/fuel-station/domain/manager-fuel-station-context.service';
import { MessageService } from 'primeng/api';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import FuelStationContext from '../../../modules/fuel-station/domain/fuel-station-context.model';
import { CreateFuelOrderDialogComponent } from '../../../modules/fuel-order/application/components/create-fuel-order-dialog/create-fuel-order-dialog.component';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-fuel-station-fuel-orders',
  imports: [CommonModule, TableModule, PanelModule, SkeletonModule, ButtonModule, CreateFuelOrderDialogComponent],
  templateUrl: './fuel-station-fuel-orders.component.html'
})
export class FuelStationFuelOrdersComponent implements OnInit {

  private messageService: MessageService = inject(MessageService);
  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);
  
  ngOnInit(): void {
    this.getFuelOrders();
  }

  get loading$(): Observable<boolean> {
    return this.ctxService.loading.fuelOrders;
  }

  get ctx$(): Observable<FuelStationContext | null>  {
    return this.ctxService.getContext();
  }

  private getFuelOrders() {
    this.ctxService.getFuelOrders()
      .subscribe({
        error: () => {
          this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching fuel orders"})
        }
      }) 
  }

}
