import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { FuelStationFuelPriceHistoryEntry, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetFuelPriceHistory } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class GetFuelPriceHistoryHandler
    extends CommandHandler<GetFuelPriceHistory, FuelStationFuelPriceHistoryEntry[]> {

    private readonly api = inject(FuelStationRestClient);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId }: GetFuelPriceHistory): Observable<FuelStationFuelPriceHistoryEntry[]> {
        return this.api.getFuelPriceHistory(fuelStationId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'An error occurred while fetching price history'
                });
                return throwError(() => e);
            })
        );
    }
}
