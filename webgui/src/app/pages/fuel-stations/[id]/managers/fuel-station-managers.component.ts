import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';

import { ManagerFuelStationContextService } from '../../../../modules/fuel-station/application/services/manager-fuel-station-context.service';
import { ManagerFuelStationContextLoadingEvent } from '../../../../modules/fuel-station/application/models/manager-fuel-station-context-loading-event.enum';

@Component({
  selector: 'app-fuel-station-managers',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './fuel-station-managers.component.html'
})
export class FuelStationManagersComponent implements OnInit {
  private messageService: MessageService = inject(MessageService);
  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);

  visible = false;
  loading = false;
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(4).fill(null);
  
  ngOnInit(): void {
    this.getManagers();
    this.ctxService.loadingEvents$.subscribe((event) => {
      if(event?.type === ManagerFuelStationContextLoadingEvent.GET_ASSIGNED_MANAGERS) {
        this.loading = event.value;
      }
    })
  }

  openDialog() { 
    this.visible = true;
  }

  get ctx$() {
    return this.ctxService.getContext();
  }

  private getManagers() {
    this.ctxService.getAssignedManagers()
      .subscribe({
        error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while fetching managers'})
      });
  }
}
