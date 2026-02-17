import { Component, inject, OnInit } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { Router } from '@angular/router';
import { SkeletonModule } from 'primeng/skeleton';
import { CreateFuelStationDialogComponent } from '../../modules/fuel-stations/components/create-fuel-station-dialog/create-fuel-station-dialog.component';
import { FuelStation } from 'fsms-web-api';
import { GetFuelStationsHandler } from '../../modules/fuel-stations/handlers/get-fuel-stations-handler';
import { tap } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-fuel-stations-page',
  imports: [CommonModule, PanelModule, TableModule, TagModule, ButtonModule, DialogModule, CreateFuelStationDialogComponent, SkeletonModule],
  templateUrl: './fuel-stations.page.html',
})
export class FuelStationsPage implements OnInit {
  private router = inject(Router);
  private getFuelStationsHandler = inject(GetFuelStationsHandler);

  protected fuelStations: FuelStation[] = [];
  protected readonly loading = toSignal(this.getFuelStationsHandler.loading$, { initialValue: false }) ;

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);

  getSeverity(fuelStation: FuelStation): "success" | undefined {
    if(fuelStation.active) {
      return "success";
    }
    return undefined;
  }

  ngOnInit(): void {
    this.getFuelStationsHandler
      .handle({})
      .pipe(tap((fuelStations) => this.fuelStations = fuelStations))
      .subscribe();
  }

  handleViewClick(fuelStationId: number) {
    this.router.navigate(["/fuel-stations/" + fuelStationId + "/info"]);
  }
}
