import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, ViewChild } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { FuelTank } from 'fsms-web-api';
import { TranslatePipe } from '@ngx-translate/core';
import { FuelStationStore } from '../../../../modules/fuel-station/fuel-station-store';
import { DecommissionFuelTankHandler } from '../../../../modules/fuel-station/handlers/decommission-fuel-tank.handler';
import { DispenseFuelDialogComponent } from '../../../../modules/fuel-station/components/dispense-fuel-dialog/dispense-fuel-dialog.component';
import { InstallFuelTankDialogComponent } from '../../../../modules/fuel-station/components/install-fuel-tank-dialog/install-fuel-tank-dialog.component';
import { FuelGradeLabel } from '../../../../modules/fuel-prices/components/fuel-grade-label/fuel-grade-label';
import { AppDatePipe } from '../../../../modules/common/app-date.pipe';

@Component({
  selector: 'app-fuel-station-fuel-tanks-page',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule, TooltipModule, DispenseFuelDialogComponent, InstallFuelTankDialogComponent, TranslatePipe, FuelGradeLabel, AppDatePipe],
  templateUrl: './fuel-station-fuel-tanks.page.html'
})
export class FuelStationFuelTanksPage {

  private readonly store = inject(FuelStationStore);
  private readonly decommissionHandler = inject(DecommissionFuelTankHandler);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly fuelStation = toSignal(this.store.fuelStation$, { initialValue: null });
  protected readonly decommissionLoading = toSignal(this.decommissionHandler.loading$, { initialValue: false });

  protected readonly skeletonRows = new Array(5).fill(null);
  protected readonly skeletonCols = new Array(5).fill(null);

  @ViewChild('dispenseDialog') dispenseDialog!: DispenseFuelDialogComponent;
  @ViewChild('installDialog') installDialog!: InstallFuelTankDialogComponent;

  openDispenseDialog(tank: FuelTank): void {
    this.dispenseDialog.fuelStationId = this.store.fuelStation.fuelStationId;
    this.dispenseDialog.fuelTankId = tank.id;
    this.dispenseDialog.fuelGrade = tank.fuelGrade;
    this.dispenseDialog.availableVolume = tank.currentVolume;
    this.dispenseDialog.openDialog();
  }

  openInstallDialog(): void {
    this.installDialog.openDialog();
  }

  decommission(tank: FuelTank): void {
    const fuelStationId = this.store.fuelStation.fuelStationId;
    this.decommissionHandler
      .handle({ fuelStationId, fuelTankId: tank.id })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }
}
