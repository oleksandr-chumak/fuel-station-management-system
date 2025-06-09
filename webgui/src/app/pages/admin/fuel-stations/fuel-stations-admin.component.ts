import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';

import { FuelStation } from '../../../modules/fuel-station/api/models/fuel-station.model';
import { CreateFuelStationDialogComponent } from '../../../modules/fuel-station/application/components/create-fuel-station-dialog/create-fuel-station-dialog.component';
import { FuelStationService } from '../../../modules/fuel-station/application/services/fuel-station.service';

@Component({
  selector: 'app-fuel-station-admin',
  imports: [CommonModule, PanelModule, TableModule, TagModule, ButtonModule, DialogModule, CreateFuelStationDialogComponent, SkeletonModule],
  templateUrl: './fuel-stations-admin.component.html',
})
export class FuelStationsAdminComponent implements OnInit {
  private messageService = inject(MessageService);
  private router: Router = inject(Router);
  private fuelStationService: FuelStationService = inject(FuelStationService);

  fuelStations: FuelStation[] = [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);

  getSeverity(fuelStation: FuelStation): 'success' | undefined {
    if(fuelStation.active) {
      return 'success';
    }
    return undefined;
  }

  ngOnInit(): void {
    this.getFuelStations();
  }

  getFuelStations() {
    this.fuelStationService.getFuelStations()
      .subscribe({
        error: () => this.messageService.add({ severity: 'error', 'summary': 'Error', detail: 'An error occurred while fetching fuel stations'}),
        next: (fuelStations) => this.fuelStations = fuelStations
      });
  }

  handleViewClick(fuelStationId: number) {
    this.router.navigate(['admin/fuel-station/' + fuelStationId + '/info']);
  }

  get loading$() {
    return this.fuelStationService.loading$;
  }
}
