import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { FuelStationFuelPriceHistoryEntry, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetFuelPriceHistory } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class GetFuelPriceHistoryHandler
    extends CommandHandler<GetFuelPriceHistory, FuelStationFuelPriceHistoryEntry[]> {

    private readonly api = inject(FuelStationRestClient);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, from }: GetFuelPriceHistory): Observable<FuelStationFuelPriceHistoryEntry[]> {
        return this.api.getFuelPriceHistory(fuelStationId, from).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: 'error',
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.fuelPriceHistoryErrorDetail')
                });
                return throwError(() => e);
            })
        );
    }
}
