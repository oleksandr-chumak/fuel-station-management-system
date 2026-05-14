import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, tap, throwError } from 'rxjs';
import { MessageService } from 'primeng/api';
import { FuelStationRestClient, FuelPurchase } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetFuelStationFuelPurchases } from '../fuel-station-commands';
import { FuelStationStore } from '../stores/fuel-station-store';

@Injectable({ providedIn: 'root' })
export class GetFuelStationFuelPurchasesHandler extends CommandHandler<GetFuelStationFuelPurchases, FuelPurchase[]> {
    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId }: GetFuelStationFuelPurchases): Observable<FuelPurchase[]> {
        return this.api.getFuelStationFuelPurchases(fuelStationId).pipe(
            catchError(e => {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred while fetching fuel purchases' });
                return throwError(() => e);
            }),
            tap(purchases => this.store.fuelPurchases = purchases)
        );
    }
}
