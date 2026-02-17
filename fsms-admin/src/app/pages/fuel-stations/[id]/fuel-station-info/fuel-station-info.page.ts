import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { InputTextModule } from 'primeng/inputtext';
import { IftaLabelModule } from 'primeng/iftalabel';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';

@Component({
  selector: 'app-fuel-station-info-page',
  imports: [CommonModule, PanelModule, InputTextModule, IftaLabelModule],
  templateUrl: './fuel-station-info.page.html'
})
export class FuelStationInfoPage {
  private fuelStationStore = inject(FuelStationStore);

  fuelStation$ = this.fuelStationStore.fuelStation$;

}
