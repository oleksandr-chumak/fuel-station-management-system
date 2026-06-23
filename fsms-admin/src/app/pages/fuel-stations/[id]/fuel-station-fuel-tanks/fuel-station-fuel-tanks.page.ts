import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, ViewChild } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { FuelGrade, FuelTank } from 'fsms-web-api';
import { FuelStationStore } from '../../../../modules/fuel-stations/stores/fuel-station-store';
import { FuelTankTemplate } from "../../../../modules/fuel-stations/directives/fuel-tank-template-directive";
import { DecommissionFuelTankHandler } from '../../../../modules/fuel-stations/handlers/decommission-fuel-tank-handler';
import { DispenseFuelDialogComponent } from '../../../../modules/fuel-stations/components/dispense-fuel-dialog/dispense-fuel-dialog.component';
import { InstallFuelTankDialogComponent } from '../../../../modules/fuel-stations/components/install-fuel-tank-dialog/install-fuel-tank-dialog.component';

@Component({
  selector: 'app-fuel-station-fuel-tanks-page',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule, TooltipModule, FuelTankTemplate, DispenseFuelDialogComponent, InstallFuelTankDialogComponent],
  templateUrl: './fuel-station-fuel-tanks.component.html'
})
export class FuelStationFuelTanksPage {
  private fuelStationStore = inject(FuelStationStore);
  private decommissionHandler = inject(DecommissionFuelTankHandler);
  private destroyRef = inject(DestroyRef);

  fuelStation$ = this.fuelStationStore.fuelStation$;

  protected readonly decommissionLoading = toSignal(this.decommissionHandler.loading$, { initialValue: false });

  @ViewChild('dispenseDialog') dispenseDialog!: DispenseFuelDialogComponent;
  @ViewChild('installDialog') installDialog!: InstallFuelTankDialogComponent;

  getFuelGradeValue(fuelGrade: FuelGrade) {
    return FuelGrade[fuelGrade];
  }

  openDispenseDialog(tank: FuelTank): void {
    this.dispenseDialog.fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
    this.dispenseDialog.fuelTankId = tank.id;
    this.dispenseDialog.fuelGrade = tank.fuelGrade;
    this.dispenseDialog.availableVolume = tank.currentVolume;
    this.dispenseDialog.openDialog();
  }

  openInstallDialog(): void {
    this.installDialog.openDialog();
  }

  decommission(tank: FuelTank): void {
    const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
    this.decommissionHandler
      .handle({ fuelStationId, fuelTankId: tank.id })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }
}
