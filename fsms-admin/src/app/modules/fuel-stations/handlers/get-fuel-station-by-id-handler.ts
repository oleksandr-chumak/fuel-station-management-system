import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStationRestClient, FuelStation } from 'fsms-web-api';
import { FuelStationStore } from '../fuel-station-store';
import { GetFuelStationById } from '../fuel-station-commands';
import { CommandHandler } from '../../common/command-handler';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class GetFuelStationByIdHandler
    extends CommandHandler<GetFuelStationById, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId }: GetFuelStationById): Observable<FuelStation> {
        return this.api.getFuelStationById(fuelStationId).pipe(
            catchError((e) => {
                if(e instanceof HttpErrorResponse && e.status == 404) {
                    this.messageService.add({
                        severity: "error", 
                        summary: "Not found", 
                        detail: "Fuel station with id: " + fuelStationId + " doesn't exist"
                    })
                }

                this.messageService.add({severity: "error", summary: "Error", detail: "Failed to fetch fuel station with id: " + fuelStationId})

                return throwError(() => e);
            }),
            tap(fuelStation => this.store.fuelStation = fuelStation)
        );
    }
}