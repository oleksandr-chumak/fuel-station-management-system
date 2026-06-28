import { CommonModule } from '@angular/common';
import { Component, inject, Signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelStation } from 'fsms-web-api';
import { TranslatePipe } from '@ngx-translate/core';
import { FuelStationStore } from '../../../../modules/fuel-station/fuel-station-store';
import { MoneyPipe } from '../../../../modules/common/money.pipe';
import { FuelGradeLabel } from '../../../../modules/fuel-prices/components/fuel-grade-label/fuel-grade-label';

@Component({
  selector: 'app-fuel-station-fuel-prices-page',
  imports: [CommonModule, TableModule, InputTextModule, SkeletonModule, PanelModule, FormsModule, ButtonModule, MoneyPipe, TranslatePipe, FuelGradeLabel],
  templateUrl: './fuel-station-fuel-prices.page.html'
})
export class FuelStationFuelPricesPage {
  private readonly store = inject(FuelStationStore);

  protected readonly fuelStation: Signal<FuelStation | null> = toSignal(this.store.fuelStation$, { initialValue: null });

  protected readonly skeletonRows = new Array(5).fill(null);
  protected readonly skeletonCols = new Array(3).fill(null);
}
