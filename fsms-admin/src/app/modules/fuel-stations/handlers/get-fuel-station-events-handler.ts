import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { DomainEventResponse, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetFuelStationEvents } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class GetFuelStationEventsHandler
    extends CommandHandler<GetFuelStationEvents, DomainEventResponse[]> {

    private readonly api = inject(FuelStationRestClient);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId, occurredAfter }: GetFuelStationEvents): Observable<DomainEventResponse[]> {
        return this.api.getFuelStationEvents(fuelStationId, occurredAfter).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'An error occurred while fetching fuel station events'
                });
                return throwError(() => e);
            })
        );
    }
}
