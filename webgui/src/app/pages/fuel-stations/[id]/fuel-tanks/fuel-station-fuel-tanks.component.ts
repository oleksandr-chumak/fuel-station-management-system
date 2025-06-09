import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';

import FuelGrade from '../../../../modules/common/fuel-grade.enum';
import ManagerFuelStationContextService from '../../../../modules/fuel-station/services/manager-fuel-station-context.service';

@Component({
  selector: 'app-fuel-station-fuel-tanks',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './fuel-station-fuel-tanks.component.html'
})
export class FuelStationFuelTanksComponent {
  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);

  visible = false;
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(4).fill(null);

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

  get ctx$() {
    return this.ctxService.getContext();
  }

  get loading$() {
    return this.ctxService.loading$;
  }
}
