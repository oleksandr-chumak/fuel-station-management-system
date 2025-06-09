import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';

import { AdminFuelStationContextLoadingEvent } from '../../../../../modules/fuel-station/application/models/admin-fuel-station-context-loading-event.enum';
import { AdminFuelStationContextService } from '../../../../../modules/fuel-station/application/services/admin-fuel-station-context.service';
import { AssignManagerDialogComponent } from '../../../../../modules/manager/application/components/assign-manager-dialog/assign-manager-dialog.component';

@Component({
  selector: 'app-admin-fuel-station-managers',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule, AssignManagerDialogComponent],
  templateUrl: './admin-fuel-station-managers.component.html'
})
export class AdminFuelStationManagersComponent implements OnInit {
  private messageService: MessageService = inject(MessageService);
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);
  loading = false;
  unassignManagerLoading = false;
  
  ngOnInit(): void {
    this.getManagers();
    this.ctxService.loadingEvents$.subscribe((event) => {
      if(event?.type === AdminFuelStationContextLoadingEvent.GET_ASSIGNED_MANAGERS) {
        this.loading = event.value;
      } else if(event?.type === AdminFuelStationContextLoadingEvent.UNASSIGN_MANAGER) {
        this.loading = event.value;
      }
    });
  }

  unassignManger(managerId: number) {
    this.ctxService.unassignManager(managerId)
      .subscribe({
        next: () => this.messageService.add({ severity: 'success', summary: 'Unassigned', detail: 'Manager was successfully unassigned' }),
        error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while unassign manager'})
      });
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
