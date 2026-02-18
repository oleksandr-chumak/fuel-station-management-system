import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, Signal } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';
import { SkeletonModule } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { toSignal } from '@angular/core/rxjs-interop';
import { GetAssignedManagersHandler } from '../../../../modules/fuel-station/handlers/get-assigned-managers.handler';
import { FuelStationStore } from '../../../../modules/fuel-station/fuel-station-store';

@Component({
  selector: 'app-fuel-station-managers-page',
  imports: [CommonModule, TableModule, PanelModule, ButtonModule, SkeletonModule],
  templateUrl: './fuel-station-managers.page.html'
})
export class FuelStationManagersPage implements OnInit {

  private readonly handler = inject(GetAssignedManagersHandler);
  private readonly store = inject(FuelStationStore);

  protected readonly managers = toSignal(this.store.managers$, { initialValue: [] });
  protected readonly loading = toSignal(this.handler.loading$, { initialValue: false });

  protected readonly skeletonRows = new Array(5).fill(null);
  protected readonly skeletonCols = new Array(4).fill(null);

  ngOnInit(): void {
    const fuelStationId = this.store.fuelStation.fuelStationId;
    this.handler
      .handle({ fuelStationId })
      .subscribe();
  }
}
