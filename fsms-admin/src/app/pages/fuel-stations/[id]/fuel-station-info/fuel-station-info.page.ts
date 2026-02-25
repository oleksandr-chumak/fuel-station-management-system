import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { InputTextModule } from 'primeng/inputtext';
import { IftaLabelModule } from 'primeng/iftalabel';
import { ButtonModule } from 'primeng/button';
import { FuelStationStore } from '../../../../modules/fuel-stations/fuel-station-store';

@Component({
  selector: 'app-fuel-station-info-page',
  imports: [CommonModule, PanelModule, InputTextModule, IftaLabelModule, ButtonModule],
  templateUrl: './fuel-station-info.page.html'
})
export class FuelStationInfoPage {
  private readonly fuelStationStore = inject(FuelStationStore);

  protected readonly fuelStation$ = this.fuelStationStore.fuelStation$;
}
