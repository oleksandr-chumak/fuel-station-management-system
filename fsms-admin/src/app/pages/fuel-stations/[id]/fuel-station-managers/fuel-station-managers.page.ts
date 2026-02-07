import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';
import AdminFuelStationContextService from '../../../../modules/fuel-stations/services/admin-fuel-station-context.service';
import { AssignManagerDialogComponent } from '../../../../modules/managers/components/assign-manager-dialog/assign-manager-dialog.component';

@Component({
  selector: 'app-fuel-station-managers-page',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule, AssignManagerDialogComponent],
  templateUrl: './fuel-station-managers.page.html'
})
export class FuelStationManagersPage implements OnInit {

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
