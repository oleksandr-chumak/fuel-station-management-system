import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { FuelGrade } from 'fsms-web-api';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';
import { FuelTankTemplate } from "../../../../modules/fuel-stations/directives/fuel-tank-template-directive";

@Component({
  selector: 'app-fuel-station-fuel-tanks-page',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule, FuelTankTemplate],
  templateUrl: './fuel-station-fuel-tanks.component.html'
})
export class FuelStationFuelTanksPage {
  private fuelStationStore = inject(FuelStationStore);

  fuelStation$ = this.fuelStationStore.fuelStation$;

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

}
