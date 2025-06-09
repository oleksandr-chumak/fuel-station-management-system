import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';

import { FuelGrade } from '../../../../../modules/common/api/fuel-grade.enum';
import { AdminFuelStationContextService } from '../../../../../modules/fuel-station/application/services/admin-fuel-station-context.service';
import { AdminFuelStationContextLoadingEvent } from '../../../../../modules/fuel-station/application/models/admin-fuel-station-context-loading-event.enum';

@Component({
  selector: 'app-admin-fuel-station-fuel-tanks',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './admin-fuel-station-fuel-tanks.component.html'
})
export class AdminFuelStationFuelTanksComponent implements OnInit {
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);

  visible = false;
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(4).fill(null);
  loading = false;

  ngOnInit(): void {
    this.ctxService.loadingEvents$.subscribe((event) => {
      if(event?.type === AdminFuelStationContextLoadingEvent.GET_FUEL_STATION) {
        this.loading = event.value;
      }
    })
  }

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

  get ctx$() {
    return this.ctxService.getContext();
  }

}
