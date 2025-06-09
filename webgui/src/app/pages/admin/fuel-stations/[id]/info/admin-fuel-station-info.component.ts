import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { IftaLabelModule } from 'primeng/iftalabel';
import { InputTextModule } from 'primeng/inputtext';
import { PanelModule } from 'primeng/panel';

import { AdminFuelStationContextService } from '~fuel-station/application/services/admin-fuel-station-context.service';

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
