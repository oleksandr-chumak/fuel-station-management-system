import { Component, DestroyRef, inject, OnDestroy, OnInit } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { AssignManagerDialogComponent } from '../../../../modules/managers/components/assign-manager-dialog/assign-manager-dialog.component';
import { ManagerTable } from '../../../../modules/managers/components/manager-table/manager-table';
import { UnassignManagerButton } from '../../../../modules/fuel-stations/components/unassign-manager-button/unassign-manager-button';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';
import { GetAssignedManagersHandler } from '../../../../modules/fuel-stations/handlers/get-assigned-managers-handler';
import { ManagerTemplateDirective } from "../../../../modules/managers/directives/manager-template-directive";

@Component({
  selector: 'app-fuel-station-managers-page',
  imports: [PanelModule, ButtonModule, AssignManagerDialogComponent, ManagerTable, UnassignManagerButton, ManagerTemplateDirective],
  templateUrl: './fuel-station-managers.page.html'
})
export class FuelStationManagersPage implements OnInit, OnDestroy {
  private readonly destroyRef = inject(DestroyRef);
  private readonly fuelStationStore = inject(FuelStationStore);
  private readonly getAssignedManagersHandler = inject(GetAssignedManagersHandler);

  protected readonly managers = toSignal(this.fuelStationStore.managers$, { initialValue: [] });
  protected readonly loading = toSignal(this.getAssignedManagersHandler.loading$, { initialValue: false });

  ngOnInit(): void {
    const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
    this.getAssignedManagersHandler
      .handle({ fuelStationId })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }

  ngOnDestroy(): void {
    this.fuelStationStore.resetManagers();
  }
}
