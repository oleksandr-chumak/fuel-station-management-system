import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStation, FuelStationRestClient } from 'fsms-web-api';
import { FuelStationStore } from '../fuel-station-store';
import { CommandHandler } from '../../common/command-handler';
import { ChangeFuelPrice } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class ChangeFuelPriceHandler
    extends CommandHandler<ChangeFuelPrice, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);

    execute({ fuelStationId, fuelGrade, newPrice }: ChangeFuelPrice): Observable<FuelStation> {
        return this.api.changeFuelPrice(fuelStationId, fuelGrade, newPrice).pipe(
            catchError((e) => {
                this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching fuel orders"})
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({ severity: "success", summary: "Changed", detail: "Fuel price was successfully changed" });
                this.store.fuelStation = fuelStation.clone();
            })
        );
    }
}