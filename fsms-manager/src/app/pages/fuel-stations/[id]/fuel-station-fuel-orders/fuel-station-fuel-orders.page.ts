import { Component, inject, OnInit, Signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { toSignal } from '@angular/core/rxjs-interop';
import { TranslatePipe } from '@ngx-translate/core';
import { CreateFuelOrderDialogComponent } from '../../../../modules/fuel-order/components/create-fuel-order-dialog/create-fuel-order-dialog.component';
import { GetFuelStationOrdersHandler } from '../../../../modules/fuel-station/handlers/get-fuel-station-orders.handler';
import { FuelStationStore } from '../../../../modules/fuel-station/fuel-station-store';
import { FuelGradeLabel } from '../../../../modules/fuel-prices/components/fuel-grade-label/fuel-grade-label';
import { FuelOrderStatusTag } from '../../../../modules/fuel-order/components/fuel-order-status-tag/fuel-order-status-tag';
import { AppDatePipe } from '../../../../modules/common/app-date.pipe';

@Component({
  selector: 'app-fuel-station-fuel-orders-page',
  imports: [CommonModule, TagModule, TableModule, PanelModule, SkeletonModule, ButtonModule, CreateFuelOrderDialogComponent, TranslatePipe, FuelGradeLabel, FuelOrderStatusTag, AppDatePipe],
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
}
