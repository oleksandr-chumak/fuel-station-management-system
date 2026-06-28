import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, tap, throwError } from 'rxjs';
import { MessageService } from 'primeng/api';
import { FuelStationRestClient, FuelPurchase } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetFuelStationFuelPurchases } from '../fuel-station-commands';
import { FuelStationStore } from '../stores/fuel-station-store';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class GetFuelStationFuelPurchasesHandler extends CommandHandler<GetFuelStationFuelPurchases, FuelPurchase[]> {
    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId }: GetFuelStationFuelPurchases): Observable<FuelPurchase[]> {
        return this.api.getFuelStationFuelPurchases(fuelStationId).pipe(
            catchError(e => {
                this.messageService.add({
                    severity: 'error',
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.fuelPurchasesErrorDetail')
                });
                return throwError(() => e);
            }),
            tap(purchases => this.store.fuelPurchases = purchases)
        );
    }
}
