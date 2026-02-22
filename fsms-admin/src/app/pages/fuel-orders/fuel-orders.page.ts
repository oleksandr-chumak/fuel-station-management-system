import { Component, DestroyRef, inject, OnDestroy, OnInit } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { ConfirmFuelOrderHandler } from '../../modules/fuel-orders/handlers/confirm-fuel-order-handler';
import { RejectFuelOrderHandler } from '../../modules/fuel-orders/handlers/reject-fuel-order-handler';
import { GetFuelOrdersHandler } from '../../modules/fuel-orders/handlers/get-fuel-orders-handler';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { FuelOrdersStore } from '../../modules/fuel-orders/fuel-orders-store';
import { tap } from 'rxjs';
import { FuelOrderTable } from "../../modules/fuel-orders/components/fuel-order-table/fuel-order-table";
import { FuelOrderEventHandler } from '../../modules/fuel-orders/fuel-order-event-handler';

@Component({
  selector: 'app-fuel-orders',
  imports: [PanelModule, FuelOrderTable],
  templateUrl: './fuel-orders.page.html'
})
export class FuelOrdersPage implements OnInit, OnDestroy {
  private readonly destroyRef = inject(DestroyRef)

  private readonly store = inject(FuelOrdersStore);
  private readonly getFuelOrdersHandler = inject(GetFuelOrdersHandler);
  private readonly confirmFuelOrderHandler = inject(ConfirmFuelOrderHandler);
  private readonly rejectFuelOrderHandler = inject(RejectFuelOrderHandler);
  private readonly fuelOrderEventHandler = inject(FuelOrderEventHandler);

  protected readonly fuelOrders = toSignal(this.store.fuelOrders$, { initialValue: [] });
  protected readonly fetchingFuelOrders = toSignal(this.getFuelOrdersHandler.loading$, { initialValue: false });
  protected readonly confirmingFuelOrder = toSignal(this.confirmFuelOrderHandler.loading$, { initialValue: false });
  protected readonly rejectingFuelOrder = toSignal(this.rejectFuelOrderHandler.loading$, { initialValue: false });

  ngOnInit(): void {
    this.fetchFuelOrders();
    this.handleFuelOrderEvents();
  }

  ngOnDestroy(): void {
    this.store.reset();
  }

  private handleFuelOrderEvents() {
    this.fuelOrderEventHandler.start()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe()
  }

  private fetchFuelOrders() {
    this.getFuelOrdersHandler
      .handle({})
      .pipe(
        tap((fuelOrders) => this.store.fuelOrders = fuelOrders),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe()
  }

  protected confirmFuelOrder(fuelOrderId: number) {
    this.confirmFuelOrderHandler
      .handle({ fuelOrderId })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }

  protected rejectFuelOrder(fuelOrderId: number) {
    this.rejectFuelOrderHandler
      .handle({ fuelOrderId })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }
  
}
