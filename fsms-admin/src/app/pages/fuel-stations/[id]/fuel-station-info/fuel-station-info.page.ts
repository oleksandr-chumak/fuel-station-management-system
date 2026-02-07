import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { InputTextModule } from 'primeng/inputtext';
import { IftaLabelModule } from 'primeng/iftalabel';
import AdminFuelStationContextService from '../../../../modules/fuel-stations/services/admin-fuel-station-context.service';

@Component({
  selector: 'app-fuel-station-info-page',
  imports: [CommonModule, PanelModule, InputTextModule, IftaLabelModule],
  templateUrl: './fuel-station-info.page.html'
})
export class FuelStationInfoPage {
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);
  
  get ctx$() {
    return this.ctxService.getContext();
  }

}
