import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStationRestClient, Manager } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetAssignedManagers } from '../fuel-station-commands';
import { FuelStationStore } from '../fuel-station-store';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class GetAssignedManagersHandler
    extends CommandHandler<GetAssignedManagers, Manager[]> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);

    execute({ fuelStationId }: GetAssignedManagers): Observable<Manager[]> {
        return this.api.getAssignedManagers(fuelStationId).pipe(
            catchError((e) => {
                this.messageService.add({ 
                    severity: "error", 
                    summary: "Error", 
                    detail: "An error occurred while fetching managers"
                });
                return throwError(() => e);
            }),
            tap(managers => this.store.managers = managers)
        );
    }
}