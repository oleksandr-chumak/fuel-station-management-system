import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { finalize } from 'rxjs';

import { FuelGrade } from '~common/api/fuel-grade.enum';
import { FuelOrderApiService } from '~fuel-order/api/fuel-order-api.service';
import { FuelOrderStatus } from '~fuel-order/api/models/fuel-order-status.enum';
import { FuelOrder } from '~fuel-order/api/models/fuel-order.model';

@Component({
  selector: 'app-admin-fuel-orders',
  imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule],
  templateUrl: './admin-fuel-orders.component.html'
})
export class AdminFuelOrdersComponent implements OnInit {
  private fuelOrderApi: FuelOrderApiService = inject(FuelOrderApiService);
  private messageService: MessageService = inject(MessageService);

  loading = false;
  actionLoading = false;
  fuelOrders: FuelOrder[] = [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);

  ngOnInit(): void {
    this.getFuelOrders();
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

  confirmFuelOrder(fuelOrderId: number) {
    this.actionLoading = true;
    this.fuelOrderApi.confirmFuelOrder(fuelOrderId)
      .pipe(finalize(() => this.actionLoading = false))
      .subscribe({
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while confirming fuel order'});
        },
        next: () => {
          this.getFuelOrders();
          this.messageService.add({ severity: 'success', summary: 'Confirmed', detail: 'Fuel orders was successfully confirmed'});
        }
      });
  }

  rejectFuelOrder(fuelOrderId: number) {
    this.actionLoading = true;
    this.fuelOrderApi.rejectFuelOrder(fuelOrderId)
      .pipe(finalize(() => this.actionLoading = false))
      .subscribe({
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while rejecting fuel order'});
        },
        next: () => {
          this.getFuelOrders();
          this.messageService.add({ severity: 'success', summary: 'Rejected', detail: 'Fuel orders was successfully rejected'});
        }
      });
  }
  
  getValue(fuelOrderStatus: FuelOrderStatus) {
    return FuelOrderStatus[fuelOrderStatus];
  }

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

  private getFuelOrders() {
    this.loading = true;
    this.fuelOrderApi.getFuelOrders()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while fetching fuel orders'});
        },
        next: (data) => {
          this.fuelOrders = data;
        }
      }); 
  }
}
