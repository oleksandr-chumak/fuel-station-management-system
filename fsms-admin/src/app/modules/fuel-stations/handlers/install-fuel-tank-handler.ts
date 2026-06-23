import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStation, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { InstallFuelTank } from '../fuel-station-commands';
import { FuelStationStore } from '../stores/fuel-station-store';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class InstallFuelTankHandler
    extends CommandHandler<InstallFuelTank, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId, fuelGrade, maxCapacity }: InstallFuelTank): Observable<FuelStation> {
        return this.api.installFuelTank(fuelStationId, fuelGrade, maxCapacity).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'An error occurred while installing fuel tank'
                });
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Installed',
                    detail: `Fuel tank installed (capacity ${maxCapacity} L)`
                });
                this.store.fuelStation = fuelStation;
            })
        );
    }
}
