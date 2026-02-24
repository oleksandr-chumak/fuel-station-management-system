import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStation, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { DeactivateFuelStation } from '../fuel-station-commands';
import { FuelStationStore } from '../fuel-station-store';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class DeactivateFuelStationHandler
    extends CommandHandler<DeactivateFuelStation, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId }: DeactivateFuelStation): Observable<FuelStation> {
        return this.api.deactivateFuelStation(fuelStationId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: "Error",
                    detail: "An error occurred while deactivating fuel station"
                });
                return throwError(() => e);
            }),
            tap(fuelStation => {
                this.messageService.add({
                    severity: "success",
                    summary: "Deactivated",
                    detail: "Fuel station was successfully deactivated"
                });
                this.store.fuelStation = fuelStation;
                this.store.resetManagers();
            })
        );
    }
}
