import { CommonModule } from "@angular/common";
import { Component, computed, input, output, viewChild } from "@angular/core";
import { RouterLink } from "@angular/router";
import { FuelOrder } from "fsms-web-api";
import { ButtonModule } from "primeng/button";
import { PanelModule } from "primeng/panel";
import { SkeletonModule } from "primeng/skeleton";
import { TableModule } from "primeng/table";
import { FuelTankTemplate } from "../../directives/fuel-order-template-directive";
import { TranslatePipe } from "@ngx-translate/core";
import { FuelOrderStatusTag } from "../fuel-order-status-tag/fuel-order-status-tag";
import { FuelGradeLabel } from "../../../fuel-prices/components/fuel-grade-label/fuel-grade-label";
import { AppDatePipe } from "../../../common/app-date.pipe";
import { ConfirmFuelOrderDialogComponent } from "../confirm-fuel-order-dialog/confirm-fuel-order-dialog.component";

@Component({
  selector: 'app-fuel-order-table',
  imports: [CommonModule, RouterLink, TableModule, PanelModule, SkeletonModule, ButtonModule, FuelTankTemplate, TranslatePipe, FuelOrderStatusTag, FuelGradeLabel, AppDatePipe, ConfirmFuelOrderDialogComponent],
  templateUrl: './fuel-order-table.html'
})
export class FuelOrderTable {
    confirmFuelOrderClicked = output<{ fuelOrderId: number; pricePerLiter: number }>()
    rejectFuelOrderClicked = output<number>()

    fuelOrders = input<FuelOrder[]>([]);
    fetchingFuelOrders = input<boolean>(false);
    confirmingFuelOrder = input<boolean>(false);
    rejectingFuelOrder = input<boolean>(false);
    showFuelStation = input<boolean>(false);

    protected readonly confirmDialog = viewChild.required(ConfirmFuelOrderDialogComponent);

    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = computed(() => new Array(this.showFuelStation() ? 7 : 6).fill(null));

    protected openConfirmDialog(order: FuelOrder) {
        this.confirmDialog().openFor(order);
    }

    protected onConfirmed(event: { fuelOrderId: number; pricePerLiter: number }) {
        this.confirmFuelOrderClicked.emit(event);
    }

    protected rejectFuelOrder(fuelOrderId: number) {
        this.rejectFuelOrderClicked.emit(fuelOrderId);
    }

}
