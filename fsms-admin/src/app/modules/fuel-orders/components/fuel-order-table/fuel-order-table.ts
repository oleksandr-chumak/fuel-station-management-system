import { CommonModule } from "@angular/common";
import { Component, computed, input, output } from "@angular/core";
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

@Component({
  selector: 'app-fuel-order-table',
  imports: [CommonModule, RouterLink, TableModule, PanelModule, SkeletonModule, ButtonModule, FuelTankTemplate, TranslatePipe, FuelOrderStatusTag, FuelGradeLabel, AppDatePipe],
  templateUrl: './fuel-order-table.html'
})
export class FuelOrderTable {
    confirmFuelOrderClicked = output<number>()
    rejectFuelOrderClicked = output<number>()

    fuelOrders = input<FuelOrder[]>([]);
    fetchingFuelOrders = input<boolean>(false);
    confirmingFuelOrder = input<boolean>(false);
    rejectingFuelOrder = input<boolean>(false);
    showFuelStation = input<boolean>(false);

    protected readonly skeletonRows = new Array(5).fill(null);
    protected readonly skeletonCols = computed(() => new Array(this.showFuelStation() ? 7 : 6).fill(null));

    protected confirmFuelOrder(fuelOrderId: number) {
        this.confirmFuelOrderClicked.emit(fuelOrderId);
    }

    protected rejectFuelOrder(fuelOrderId: number) {
        this.rejectFuelOrderClicked.emit(fuelOrderId);
    }

}