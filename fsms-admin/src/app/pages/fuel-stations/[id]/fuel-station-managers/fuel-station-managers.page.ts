import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';
import { AssignManagerDialogComponent } from '../../../../modules/managers/components/assign-manager-dialog/assign-manager-dialog.component';
import { UnassignManagerHandler } from '../../../../modules/fuel-stations/handlers/unassign-manager-handler';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';
import { GetAssignedManagersHandler } from '../../../../modules/fuel-stations/handlers/get-assigned-managers-handler';

@Component({
  selector: 'app-fuel-station-managers-page',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule, AssignManagerDialogComponent],
  templateUrl: './fuel-station-managers.page.html'
})
export class FuelStationManagersPage implements OnInit, OnDestroy {

  private fuelStationStore = inject(FuelStationStore);
  private getAssignedManagersHandler = inject(GetAssignedManagersHandler);
  private unassignManagerHandler = inject(UnassignManagerHandler)

  private fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;

  managers$ = this.fuelStationStore.managers$;
  loading$ = this.getAssignedManagersHandler.loading$;
  loadingUnassignManager$ = this.unassignManagerHandler.loading$;

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);
  
  ngOnInit(): void {
    this.getAssignedManagersHandler
      .handle({ fuelStationId: this.fuelStationId })
      .subscribe()
  }

  ngOnDestroy(): void {
    this.fuelStationStore.resetManagers();
  }

  protected unassignManger(managerId: number) {
    this.unassignManagerHandler
      .handle({ fuelStationId: this.fuelStationId, managerId })
      .subscribe();
  }

}
