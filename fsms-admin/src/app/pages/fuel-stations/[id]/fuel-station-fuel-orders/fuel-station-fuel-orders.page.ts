import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { MessageService } from 'primeng/api';
import { Observable } from 'rxjs';
import { FuelOrderStatus, FuelGrade } from 'fsms-web-api';
import AdminFuelStationContextService from '../../../../modules/fuel-stations/services/admin-fuel-station-context.service';
import FuelStationContext from '../../../../modules/fuel-stations/models/fuel-station-context.model';

@Component({
  selector: 'app-fuel-station-fuel-orders-page',
  imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule],
  templateUrl: './fuel-station-fuel-orders.page.html'
})
export class FuelStationFuelOrdersPage {

  private messageService: MessageService = inject(MessageService);
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);

  actionLoading = false;
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);

  ngOnInit(): void {
    this.getFuelOrders();
    
    this.ctxService.loading.rejectOrder.subscribe((value) => this.actionLoading = value)
    this.ctxService.loading.confirmOrder.subscribe((value) => this.actionLoading = value);
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

  confirmFuelOrder(fuelOrderId: number) {
    // TODO add error handling
    this.ctxService.confirmFuelOrder(fuelOrderId).subscribe();
  }

  rejectFuelOrder(fuelOrderId: number) {
    // TODO add error handling
    this.ctxService.rejectFuelOrder(fuelOrderId).subscribe();
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
