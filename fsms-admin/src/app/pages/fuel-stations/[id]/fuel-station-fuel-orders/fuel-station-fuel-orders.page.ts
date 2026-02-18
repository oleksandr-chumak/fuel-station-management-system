import { Component, computed, DestroyRef, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';
import { GetFuelStationOrdersHandler } from '../../../../modules/fuel-stations/handlers/get-fuel-station-orders-handler';
import { ConfirmFuelStationOrderHandler } from '../../../../modules/fuel-stations/handlers/confirm-fuel-station-order-handler';
import { RejectFuelStationOrderHandler } from '../../../../modules/fuel-stations/handlers/reject-fuel-station-order-handler';
import { FuelOrderTable } from "../../../../modules/fuel-orders/components/fuel-order-table/fuel-order-table";

@Component({
    selector: 'app-fuel-station-fuel-orders-page',
    imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule, FuelOrderTable],
    templateUrl: './fuel-station-fuel-orders.page.html'
})
export class FuelStationFuelOrdersPage implements OnInit, OnDestroy {
    private readonly destroyRef = inject(DestroyRef);

    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly getFuelStationOrders = inject(GetFuelStationOrdersHandler);
    private readonly confirmFuelStationOrder = inject(ConfirmFuelStationOrderHandler);
    private readonly rejectFuelStationOrder = inject(RejectFuelStationOrderHandler);

    protected readonly fuelOrders = toSignal(this.fuelStationStore.fuelOrders$, { initialValue: [] });
    protected readonly fetchingFuelOrders = toSignal(this.getFuelStationOrders.loading$, { initialValue: false });
    protected readonly confirmingFuelOrder = toSignal(this.confirmFuelStationOrder.loading$, { initialValue: false });
    protected readonly rejectingFuelOrder = toSignal(this.rejectFuelStationOrder.loading$, { initialValue: false });

    ngOnInit(): void {
        this.fetchFuelOrders()
    }

    ngOnDestroy(): void {
        this.fuelStationStore.resetFuelOrders(); 
    }

    protected fetchFuelOrders() {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
        this.getFuelStationOrders
            .handle({ fuelStationId })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe();
    }

    protected confirmFuelOrder(fuelOrderId: number): void {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
        this.confirmFuelStationOrder
            .handle({ fuelStationId, fuelOrderId })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe();
    }

    protected rejectFuelOrder(fuelOrderId: number): void {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
        this.rejectFuelStationOrder
            .handle({ fuelStationId, fuelOrderId })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe();
    }

}