import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import AdminFuelStationContextService from '../../../../modules/fuel-order/domain/admin-fuel-station-context.service';
import { MessageService } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';

@Component({
  selector: 'app-admin-fuel-station-managers',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './admin-fuel-station-managers.component.html'
})
export class AdminFuelStationManagersComponent implements OnInit {

  private messageService: MessageService = inject(MessageService);
  private ctxService: AdminFuelStationContextService = inject(AdminFuelStationContextService);

  skeletonRows = new Array(5).fill(null);
  skeletonCols = new Array(5).fill(null);
  
  ngOnInit(): void {
    let managers = [];
    this.ctx$.subscribe((ctx) => managers = ctx ? ctx.managers : [])
    if(managers.length != 0) {
      return;
    }
    this.getManagers();
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
