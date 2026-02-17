import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { FuelOrderStatus, FuelGrade } from 'fsms-web-api';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';
import { GetFuelStationOrdersHandler } from '../../../../modules/fuel-stations/handlers/get-fuel-station-orders-handler';
import { ConfirmFuelStationOrderHandler } from '../../../../modules/fuel-stations/handlers/confirm-fuel-station-order-handler';
import { RejectFuelStationOrderHandler } from '../../../../modules/fuel-stations/handlers/reject-fuel-station-order-handler';

@Component({
    selector: 'app-fuel-station-fuel-orders-page',
    imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule],
    templateUrl: './fuel-station-fuel-orders.page.html'
})
export class FuelStationFuelOrdersPage {
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly getFuelStationOrders = inject(GetFuelStationOrdersHandler);
    private readonly confirmFuelStationOrder = inject(ConfirmFuelStationOrderHandler);
    private readonly rejectFuelStationOrder = inject(RejectFuelStationOrderHandler);

    readonly fuelOrders = toSignal(this.fuelStationStore.fuelOrders$, { initialValue: [] });
    readonly loadingOrders = toSignal(this.getFuelStationOrders.loading$, { initialValue: false });
    readonly loadingAction = computed(() =>
        toSignal(this.confirmFuelStationOrder.loading$, { initialValue: false })() ||
        toSignal(this.rejectFuelStationOrder.loading$, { initialValue: false })()
    );

    readonly skeletonRows = new Array(5).fill(null);
    readonly skeletonCols = new Array(5).fill(null);

    constructor() {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;

        this.getFuelStationOrders
            .handle({ fuelStationId })
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

    getFuelOrderStatusValue(status: FuelOrderStatus): string {
        return FuelOrderStatus[status];
    }

    getFuelGradeValue(fuelGrade: FuelGrade): string {
        return FuelGrade[fuelGrade];
    }

    confirmFuelOrder(fuelOrderId: number): void {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
        this.confirmFuelStationOrder
            .handle({ fuelStationId, fuelOrderId })
            .subscribe();
    }

    rejectFuelOrder(fuelOrderId: number): void {
        const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
        this.rejectFuelStationOrder
            .handle({ fuelStationId, fuelOrderId })
            .subscribe();
    }

}