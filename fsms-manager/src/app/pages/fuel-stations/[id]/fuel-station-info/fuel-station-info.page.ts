import { Component, inject, Signal } from '@angular/core';
import { IftaLabelModule } from 'primeng/iftalabel';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import { toSignal } from '@angular/core/rxjs-interop';
import { FuelStation } from 'fsms-web-api';
import { FuelStationStore } from '../../../../modules/fuel-station/fuel-station-store';

@Component({
  selector: 'app-fuel-station-info-page',
  imports: [PanelModule, InputTextModule, IftaLabelModule],
  templateUrl: './fuel-station-info.page.html'
})
export class FuelStationInfoPage {
  private readonly store = inject(FuelStationStore);

  protected readonly fuelStation: Signal<FuelStation | null> = toSignal(this.store.fuelStation$, { initialValue: null });
}
