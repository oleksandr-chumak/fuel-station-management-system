import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';

import { FuelGrade } from '../../../../modules/common/api/fuel-grade.enum';
import { ManagerFuelStationContextLoadingEvent } from '../../../../modules/fuel-station/application/models/manager-fuel-station-context-loading-event.enum';
import { ManagerFuelStationContextService } from '../../../../modules/fuel-station/application/services/manager-fuel-station-context.service';

@Component({
  selector: 'app-fuel-station-fuel-tanks',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './fuel-station-fuel-tanks.component.html'
})
export class FuelStationFuelTanksComponent implements OnInit {
  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);

  visible = false;
  loading = false;
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(4).fill(null);

  ngOnInit(): void {
    this.ctxService.loadingEvents$.subscribe((event) => {
      if(event?.type === ManagerFuelStationContextLoadingEvent.GET_FUEL_STATION) {
        this.loading = event.value;
      }
    });
  }

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

  get ctx$() {
    return this.ctxService.getContext();
  }
}
