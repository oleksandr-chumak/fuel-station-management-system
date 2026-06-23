import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStation, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { DecommissionFuelTank } from '../fuel-station-commands';
import { FuelStationStore } from '../stores/fuel-station-store';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class DecommissionFuelTankHandler
    extends CommandHandler<DecommissionFuelTank, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId, fuelTankId }: DecommissionFuelTank): Observable<FuelStation> {
        return this.api.decommissionFuelTank(fuelStationId, fuelTankId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'An error occurred while decommissioning fuel tank'
                });
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Decommissioned',
                    detail: `Fuel tank ${fuelTankId} was decommissioned`
                });
                this.store.fuelStation = fuelStation;
            })
        );
    }
}
