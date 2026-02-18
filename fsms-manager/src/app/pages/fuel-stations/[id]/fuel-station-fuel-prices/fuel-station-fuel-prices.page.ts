import { CommonModule } from '@angular/common';
import { Component, inject, Signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelGrade, FuelStation } from 'fsms-web-api';
import { FuelStationStore } from '../../../../modules/fuel-station/fuel-station-store';

@Component({
  selector: 'app-fuel-station-fuel-prices-page',
  imports: [CommonModule, TableModule, InputTextModule, SkeletonModule, PanelModule, FormsModule, ButtonModule],
  templateUrl: './fuel-station-fuel-prices.page.html'
})
export class FuelStationFuelPricesPage {
  private readonly store = inject(FuelStationStore);

  protected readonly fuelStation: Signal<FuelStation | null> = toSignal(this.store.fuelStation$, { initialValue: null });

  protected readonly skeletonRows = new Array(5).fill(null);
  protected readonly skeletonCols = new Array(3).fill(null);

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }
}
