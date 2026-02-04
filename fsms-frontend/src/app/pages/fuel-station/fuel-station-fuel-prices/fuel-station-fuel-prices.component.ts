import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import ManagerFuelStationContextService from '../../../modules/fuel-station/services/manager-fuel-station-context.service';
import { FuelGrade } from 'fsms-web-api';

@Component({
  selector: 'app-fuel-station-fuel-prices',
  imports: [CommonModule, TableModule, InputTextModule, SkeletonModule, PanelModule, FormsModule, ButtonModule],
  templateUrl: './fuel-station-fuel-prices.component.html'
})
export class FuelStationFuelPricesComponent {

  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(3).fill(null);

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

  get ctx$() {
    return this.ctxService.getContext();
  }
}
