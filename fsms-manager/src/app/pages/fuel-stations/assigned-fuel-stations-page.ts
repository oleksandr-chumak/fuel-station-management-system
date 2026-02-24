import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, OnInit, Signal } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { Router } from '@angular/router';
import { FuelStation } from 'fsms-web-api';
import { AuthService } from 'fsms-security';
import { GetAssignedFuelStationsHandler } from '../../modules/fuel-station/handlers/get-assigned-fuel-stations.handler';
import { AssignedFuelStationsStore } from '../../modules/fuel-station/assigned-fuel-stations-store';
import { ManagerEventHandler } from '../../modules/manager/manager-event-handler';

@Component({
  selector: 'app-dashboard-page',
  imports: [CommonModule, PanelModule, TableModule, TagModule, ButtonModule, DialogModule, SkeletonModule],
  templateUrl: './assigned-fuel-stations-page.html',
})
export class AssignedFuelStationsPage implements OnInit {
  private readonly destroyRef = inject(DestroyRef);
  private readonly router = inject(Router);

  private readonly authService = inject(AuthService);
  private readonly getAssignedFuelStationsHandler = inject(GetAssignedFuelStationsHandler);
  private readonly assignedFuelStationsStore = inject(AssignedFuelStationsStore);
  private readonly managerEventHandler = inject(ManagerEventHandler);

  protected readonly loading: Signal<boolean> = toSignal(this.getAssignedFuelStationsHandler.loading$, { initialValue: false });
  protected readonly fuelStations: Signal<FuelStation[]> = toSignal(this.assignedFuelStationsStore.fuelStations$, { initialValue: [] });
  protected readonly skeletonRows = new Array(5).fill(null);
  protected readonly skeletonCols = new Array(5).fill(null);

  ngOnInit(): void {
    const user = this.authService.getUserValue();

    if (!user) {
      throw new Error("User not found");
    }

    this.getAssignedFuelStationsHandler.handle({ managerId: user.userId }).subscribe();

    this.handleManagerEvents(user.userId);
  }

  private handleManagerEvents(managerId: number): void {
    this.managerEventHandler.start(managerId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }

  protected handleViewClick(fuelStationId: number) {
    this.router.navigate(["fuel-station/" + fuelStationId + "/info"]);
  }

  protected getSeverity(fuelStation: FuelStation): "success" | undefined {
    if (fuelStation.active) {
      return "success";
    }
    return undefined;
  }

}
