import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { FuelStation } from '../../../modules/fuel-station/domain/fuel-station.model';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import FuelStationQueryService from '../../../modules/fuel-station/application/fuel-stations-query.service';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { CreateFuelStationDialogComponent } from '../../../modules/fuel-station/application/create-fuel-station-dialog/create-fuel-station-dialog.component';
import { Router } from '@angular/router';
import { SkeletonModule } from 'primeng/skeleton';

@Component({
  selector: 'app-fuel-station-admin',
  imports: [CommonModule, PanelModule, TableModule, TagModule, ButtonModule, DialogModule, CreateFuelStationDialogComponent, SkeletonModule],
  templateUrl: './fuel-stations-admin.component.html',
})
export class FuelStationsAdminComponent implements OnInit, OnDestroy {
  private messageService = inject(MessageService);
  private router: Router = inject(Router);
  fuelStationQueryService: FuelStationQueryService = inject(FuelStationQueryService);

  visible: boolean = false;
  fuelStations: FuelStation[] = [];
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);

  getSeverity(fuelStation: FuelStation): "success" | undefined {
    if(fuelStation.active) {
      return "success";
    }
    return undefined;
  }

  ngOnInit(): void {
    this.fuelStationQueryService.getFuelStations();
    this.fuelStationQueryService.fuelStations$.subscribe((data) => this.fuelStations = data ? data : []);
    this.fuelStationQueryService.error$.subscribe((err) => {
        if(!err) return;
        this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching fuel stations." });
      })
  }

  ngOnDestroy(): void {
    this.fuelStationQueryService.destroy();
  }

  openDialog() {
    this.visible = true;
  }

  handleViewClick(fuelStationId: number) {
    this.router.navigate(["admin/fuel-station/" + fuelStationId + "/info"]);
  }
}
