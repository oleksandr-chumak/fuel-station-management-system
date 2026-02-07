import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { FuelGrade } from 'fsms-web-api';
import AdminFuelStationContextService from '../../../../modules/fuel-stations/services/admin-fuel-station-context.service';

@Component({
  selector: 'app-fuel-station-fuel-tanks-page',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './fuel-station-fuel-tanks.component.html'
})
export class FuelStationFuelTanksPage {
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);

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
    return this.ctxService.loading.fuelStation;
  }

}
