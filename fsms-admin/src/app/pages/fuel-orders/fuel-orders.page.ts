import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnDestroy, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { FuelOrderStatus, FuelGrade } from 'fsms-web-api';
import { ConfirmFuelOrderHandler } from '../../modules/fuel-orders/handlers/confirm-fuel-order-handler';
import { RejectFuelOrderHandler } from '../../modules/fuel-orders/handlers/reject-fuel-order-handler';
import { GetFuelOrdersHandler } from '../../modules/fuel-orders/handlers/get-fuel-orders-handler';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelOrdersStore } from '../../modules/fuel-orders/fuel-orders-store';
import { tap } from 'rxjs';

@Component({
  selector: 'app-fuel-orders',
  imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule],
  templateUrl: './fuel-orders.page.html'
})
export class FuelOrdersPage implements OnInit, OnDestroy {

  private readonly store = inject(FuelOrdersStore);
  private readonly getFuelOrdersHandler = inject(GetFuelOrdersHandler);
  private readonly confirmFuelOrderHandler = inject(ConfirmFuelOrderHandler);
  private readonly rejectFuelOrderHandler = inject(RejectFuelOrderHandler);

  readonly fuelOrders = toSignal(this.store.fuelOrders$, { initialValue: [] });
  readonly loading = toSignal(this.getFuelOrdersHandler.loading$, { initialValue: false });
  readonly actionLoading = computed(() =>
      toSignal(this.confirmFuelOrderHandler.loading$, { initialValue: false })() ||
      toSignal(this.rejectFuelOrderHandler.loading$, { initialValue: false })()
  );

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(6).fill(null);

  ngOnInit(): void {
    this.getFuelOrdersHandler
      .handle({})
      .pipe(tap((fuelOrders) => this.store.fuelOrders = fuelOrders))
      .subscribe()
  }

  ngOnDestroy(): void {
    this.store.reset();
  }

  confirmFuelOrder(fuelOrderId: number) {
    this.confirmFuelOrderHandler
      .handle({ fuelOrderId })
      .subscribe();
  }

  rejectFuelOrder(fuelOrderId: number) {
    this.rejectFuelOrderHandler
      .handle({ fuelOrderId })
      .subscribe();
  }
  
  getSeverity(status: FuelOrderStatus): 'success' | 'info' | 'danger' | undefined {
      switch (status) {
          case FuelOrderStatus.Confirmed: return 'success';
          case FuelOrderStatus.Pending:   return 'info';
          case FuelOrderStatus.Rejected:  return 'danger';
          default:                        return undefined;
      }
  }

  getValue(fuelOrderStatus: FuelOrderStatus) {
    return FuelOrderStatus[fuelOrderStatus];
  }

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

}
