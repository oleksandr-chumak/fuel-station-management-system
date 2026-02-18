import { CommonModule } from "@angular/common";
import { Component, input, output } from "@angular/core";
import { FuelGrade, FuelOrder, FuelOrderStatus } from "fsms-web-api";
import { ButtonModule } from "primeng/button";
import { PanelModule } from "primeng/panel";
import { SkeletonModule } from "primeng/skeleton";
import { TableModule } from "primeng/table";
import { TagModule } from "primeng/tag";

@Component({
  selector: 'app-fuel-order-table',
  imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule],
  templateUrl: './fuel-order-table.html'
})
export class FuelOrderTable {
    confirmFuelOrderClicked = output<number>()
    rejectFuelOrderClicked = output<number>()

    fuelOrders = input<FuelOrder[]>([]);
    fetchingFuelOrders = input<boolean>(false);
    confirmingFuelOrder = input<boolean>(false);
    rejectingFuelOrder = input<boolean>(false);

    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = new Array(5).fill(null);

    protected getSeverity(status: FuelOrderStatus): 'success' | 'info' | 'danger' | undefined {
        switch (status) {
            case FuelOrderStatus.Confirmed: return 'success';
            case FuelOrderStatus.Pending:   return 'info';
            case FuelOrderStatus.Rejected:  return 'danger';
            default:                        return undefined;
        }
    }

    protected getFuelOrderStatusValue(status: FuelOrderStatus): string {
        return FuelOrderStatus[status];
    }

    protected getFuelGradeValue(fuelGrade: FuelGrade): string {
        return FuelGrade[fuelGrade];
    }

    protected confirmFuelOrder(fuelOrderId: number) {
        this.confirmFuelOrderClicked.emit(fuelOrderId);
    }

    protected rejectFuelOrder(fuelOrderId: number) {
        this.rejectFuelOrderClicked.emit(fuelOrderId);
    }

}