import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import ManagerFuelStationContextService from '../../../modules/fuel-station/services/manager-fuel-station-context.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-fuel-station-managers',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './fuel-station-managers.component.html'
})
export class FuelStationManagersComponent {

  private messageService: MessageService = inject(MessageService);
  private ctxService: ManagerFuelStationContextService = inject(ManagerFuelStationContextService);

  visible = false;
  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(4).fill(null);
  
  ngOnInit(): void {
    this.getManagers();
  }

  openDialog() { 
    this.visible = true;
  }

  get ctx$() {
    return this.ctxService.getContext();
  }

  get loading$() {
    return this.ctxService.loading.managers;
  }

  private getManagers() {
    this.ctxService.getAssignedManagers()
      .subscribe({
        error: () => this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching managers"})
      })
  }

}
