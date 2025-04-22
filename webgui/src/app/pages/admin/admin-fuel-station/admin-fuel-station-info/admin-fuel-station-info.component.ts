import { Component, inject } from '@angular/core';
import AdminFuelStationContextService from '../../../../modules/fuel-order/domain/admin-fuel-station-context.service';
import { CommonModule } from '@angular/common';
import { PanelModule } from 'primeng/panel';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { IftaLabelModule } from 'primeng/iftalabel';

@Component({
  selector: 'app-admin-fuel-station-info',
  imports: [CommonModule, PanelModule, InputTextModule, FloatLabelModule, IftaLabelModule],
  templateUrl: './admin-fuel-station-info.component.html'
})
export class AdminFuelStationInfoComponent {
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);
  
  get ctx$() {
    return this.ctxService.getContext();
  }

}
