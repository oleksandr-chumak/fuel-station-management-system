import { Component, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { InputTextModule } from 'primeng/inputtext';
import { IftaLabelModule } from 'primeng/iftalabel';
import { ButtonModule } from 'primeng/button';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';
import { DeactivateFuelStationHandler } from '../../../../modules/fuel-stations/handlers/deactivate-fuel-station-handler';

@Component({
  selector: 'app-fuel-station-info-page',
  imports: [CommonModule, PanelModule, InputTextModule, IftaLabelModule, ButtonModule],
  templateUrl: './fuel-station-info.page.html'
})
export class FuelStationInfoPage {
  private readonly destroyRef = inject(DestroyRef);
  private readonly fuelStationStore = inject(FuelStationStore);
  private readonly deactivateFuelStationHandler = inject(DeactivateFuelStationHandler);

  protected readonly fuelStation$ = this.fuelStationStore.fuelStation$;
  protected readonly deactivateLoading = toSignal(
    this.deactivateFuelStationHandler.loading$,
    { initialValue: false }
  );

  protected deactivate(): void {
    const fuelStationId = this.fuelStationStore.fuelStation.fuelStationId;
    this.deactivateFuelStationHandler
      .handle({ fuelStationId })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }
}
