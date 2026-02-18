import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { CreateFuelStationDialogComponent } from '../../modules/fuel-stations/components/create-fuel-station-dialog/create-fuel-station-dialog.component';
import { FuelStationTable } from '../../modules/fuel-stations/components/fuel-station-table/fuel-station-table';
import { FuelStation } from 'fsms-web-api';
import { GetFuelStationsHandler } from '../../modules/fuel-stations/handlers/get-fuel-stations-handler';
import { tap } from 'rxjs';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-fuel-stations-page',
  imports: [PanelModule, ButtonModule, CreateFuelStationDialogComponent, FuelStationTable],
  templateUrl: './fuel-stations.page.html',
})
export class FuelStationsPage implements OnInit {
  private readonly destroyRef = inject(DestroyRef)

  private readonly router = inject(Router);
  private readonly getFuelStationsHandler = inject(GetFuelStationsHandler);

  protected fuelStations: FuelStation[] = [];
  protected readonly loading = toSignal(this.getFuelStationsHandler.loading$, { initialValue: false });

  ngOnInit(): void {
    this.fetchFuelStations();
  }

  protected fetchFuelStations(): void {
    this.getFuelStationsHandler
      .handle({})
      .pipe(
        tap((fuelStations) => this.fuelStations = fuelStations),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe();
  }

  protected viewFuelStation(fuelStationId: number): void {
    this.router.navigate(['/fuel-stations/' + fuelStationId + '/info']);
  }
}
