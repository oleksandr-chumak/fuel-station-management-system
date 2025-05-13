import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import AdminFuelStationContextService from '../../../../modules/fuel-station/services/admin-fuel-station-context.service';
import { MessageService } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';
import { AssignManagerDialogComponent } from '../../../../modules/manager/interfaces/components/assign-manager-dialog/assign-manager-dialog.component';

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
  
  ngOnInit(): void {
    this.getManagers();
  }

  unassignManger(managerId: number) {
    this.ctxService.unassignManager(managerId)
      .subscribe({
        next: () => this.messageService.add({ severity: "success", summary: "Unassigned", detail: "Manager was successfully unassigned" }),
        error: () => this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while unassign manager"})
      })
  }

  get ctx$() {
    return this.ctxService.getContext();
  }

  get loading$() {
    return this.ctxService.loading.managers;
  }

  get loadingUnassignManager$() {
    return this.ctxService.loading.unassignManager;
  }


  private getManagers() {
    this.ctxService.getAssignedManagers()
      .subscribe({
        error: () => this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching managers"})
      })
  }

}
