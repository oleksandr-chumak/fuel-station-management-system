import { CommonModule } from '@angular/common';
import { Component, inject, Signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelGrade } from 'fsms-web-api';
import { FuelStationStore } from '../../../../modules/fuel-station/fuel-station-store';

@Component({
  selector: 'app-fuel-station-fuel-tanks-page',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './fuel-station-fuel-tanks.page.html'
})
export class FuelStationFuelTanksPage {

  private readonly store = inject(FuelStationStore);

  protected readonly fuelStation = toSignal(this.store.fuelStation$, { initialValue: null });

  protected readonly skeletonRows = new Array(5).fill(null);
  protected readonly skeletonCols = new Array(4).fill(null);

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }
}
