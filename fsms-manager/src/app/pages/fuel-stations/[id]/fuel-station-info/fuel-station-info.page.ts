import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { IftaLabelModule } from 'primeng/iftalabel';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';
import ManagerFuelStationContextService from '../../../../modules/fuel-station/services/manager-fuel-station-context.service';

@Component({
  selector: 'app-fuel-station-info-page',
  imports: [CommonModule, PanelModule, InputTextModule, IftaLabelModule],
  templateUrl: './fuel-station-info.page.html'
})
export class FuelStationInfoPage {
  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);
  
  get ctx$() {
    return this.ctxService.getContext();
  }

}
