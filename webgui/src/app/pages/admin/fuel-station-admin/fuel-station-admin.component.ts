import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { TableModule } from 'primeng/table';
import { FuelStation } from '../../../modules/fuel-station/domain/fuel-station.model';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import FuelStationQueryService from '../../../modules/fuel-station/application/fuel-station-query.service';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-fuel-station-admin',
  imports: [CommonModule, PanelModule, TableModule, TagModule, ButtonModule],
  templateUrl: './fuel-station-admin.component.html',
})
export class FuelStationAdminComponent implements OnInit, OnDestroy {
  private messageService = inject(MessageService);
  fuelStationQueryService: FuelStationQueryService = inject(FuelStationQueryService);

  getSeverity(fuelStation: FuelStation): "success" | undefined {
    if(fuelStation.active) {
      return "success";
    }
    return undefined;
  }

  ngOnInit(): void {
    this.fuelStationQueryService.getFuelStations();
    this.fuelStationQueryService.error$.subscribe((err) => {
        if(!err) return;
        this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching fuel stations." });
      })
  }

  ngOnDestroy(): void {
    this.fuelStationQueryService.destroy();
  }
}
