import { Component, inject, OnInit, Signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelGrade, FuelOrder, FuelOrderStatus } from 'fsms-web-api';
import { CreateFuelOrderDialogComponent } from '../../../../modules/fuel-order/components/create-fuel-order-dialog/create-fuel-order-dialog.component';
import { GetFuelStationOrdersHandler } from '../../../../modules/fuel-station/handlers/get-fuel-station-orders.handler';
import { FuelStationStore } from '../../../../modules/fuel-station/fuel-station-store';

@Component({
  selector: 'app-fuel-station-fuel-orders-page',
  imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule, CreateFuelOrderDialogComponent],
  templateUrl: './fuel-station-fuel-orders.page.html'
})
export class FuelStationFuelOrdersPage implements OnInit {

  private readonly getFuelStationOrdersHandler = inject(GetFuelStationOrdersHandler);
  private readonly store = inject(FuelStationStore);

  protected readonly fuelOrders = toSignal(this.store.fuelOrders$, { initialValue: [] });
  protected readonly loading = toSignal(this.getFuelStationOrdersHandler.loading$, { initialValue: false });

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);

  ngOnInit(): void {
    const fuelStationId = this.store.fuelStation.fuelStationId;
    this.getFuelStationOrdersHandler
      .handle({ fuelStationId })
      .subscribe();
  }

  getSeverity(fuelOrderStatus: FuelOrderStatus): "success" | "info" | "danger" | undefined {
    switch (fuelOrderStatus) {
      case FuelOrderStatus.Confirmed:
        return "success";
      case FuelOrderStatus.Pending:
        return "info";
      case FuelOrderStatus.Rejected:
        return "danger";
      default:
        return undefined;
    }
  }

  getValue(fuelOrderStatus: FuelOrderStatus) {
    return FuelOrderStatus[fuelOrderStatus];
  }

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }
}
