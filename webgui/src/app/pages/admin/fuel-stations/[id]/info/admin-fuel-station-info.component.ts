import { Component, inject } from '@angular/core';
import AdminFuelStationContextService from '../../../../../modules/fuel-station/services/admin-fuel-station-context.service';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { InputTextModule } from 'primeng/inputtext';
import { IftaLabelModule } from 'primeng/iftalabel';

@Component({
  selector: 'app-admin-fuel-station-info',
  imports: [CommonModule, PanelModule, InputTextModule, IftaLabelModule],
  templateUrl: './admin-fuel-station-info.component.html'
})
export class AdminFuelStationInfoComponent {
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);
  
  get ctx$() {
    return this.ctxService.getContext();
  }
}
