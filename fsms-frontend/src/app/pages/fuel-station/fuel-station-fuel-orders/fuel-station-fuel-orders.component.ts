import { Component, inject, OnInit } from '@angular/core';
import ManagerFuelStationContextService from '../../../modules/fuel-station/services/manager-fuel-station-context.service';
import { MessageService } from 'primeng/api';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { CreateFuelOrderDialogComponent } from '../../../modules/fuel-order/components/create-fuel-order-dialog/create-fuel-order-dialog.component';
import FuelStationContext from '../../../modules/fuel-station/models/fuel-station-context.model';
import { FuelGrade, FuelOrderStatus } from 'fsms-web-api';

@Component({
  selector: 'app-fuel-station-fuel-orders',
  imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule, CreateFuelOrderDialogComponent],
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

  getSeverity(fuelOrderStatus: FuelOrderStatus): "success" | "info" | "danger" | undefined {
    switch(fuelOrderStatus) {
      case FuelOrderStatus.Confirmed:
        return "success";
      case FuelOrderStatus.Pending:
        return "info";
      case FuelOrderStatus.Rejected:
        return "danger";
      default:
        return undefined
    }
  }
  
  getValue(fuelOrderStatus: FuelOrderStatus) {
    return FuelOrderStatus[fuelOrderStatus];
  }

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
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
