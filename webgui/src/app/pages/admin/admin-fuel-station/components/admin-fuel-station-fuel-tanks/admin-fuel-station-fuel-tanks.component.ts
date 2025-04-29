import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import AdminFuelStationContextService from '../../../../../modules/fuel-station/domain/admin-fuel-station-context.service';
import FuelGrade from '../../../../../modules/common/domain/fuel-grade.enum';

@Component({
  selector: 'app-admin-fuel-station-fuel-tanks',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './admin-fuel-station-fuel-tanks.component.html'
})
export class AdminFuelStationFuelTanksComponent {

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
