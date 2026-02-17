import { inject, Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { FuelStation, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { DeactivateFuelStation } from '../fuel-station-commands';
import { FuelStationStore } from '../fuel-station-store';

@Injectable({ providedIn: 'root' })
export class DeactivateFuelStationHandler
    extends CommandHandler<DeactivateFuelStation, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    execute({ fuelStationId }: DeactivateFuelStation): Observable<FuelStation> {
        return this.api.deactivateFuelStation(fuelStationId).pipe(
            tap(fuelStation => this.store.fuelStation = fuelStation)
        );
    }
}