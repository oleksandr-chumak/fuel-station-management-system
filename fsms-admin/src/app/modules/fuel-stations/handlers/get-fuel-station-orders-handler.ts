import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelOrder, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetFuelStationOrders } from '../fuel-station-commands';
import { FuelStationStore } from '../fuel-station-store';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class GetFuelStationOrdersHandler
    extends CommandHandler<GetFuelStationOrders, FuelOrder[]> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);

    execute({ fuelStationId }: GetFuelStationOrders): Observable<FuelOrder[]> {
        return this.api.getFuelStationOrders(fuelStationId).pipe(
            catchError((e) => {
                this.messageService.add({ 
                    severity: 'error', 
                    summary: 'Error', 
                    detail: 'An error occurred while fetching fuel orders' 
                });
                return throwError(() => e);
            }),
            tap(fuelOrders => this.store.fuelOrders = fuelOrders)
        );
    }
}