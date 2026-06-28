import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelOrder, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetFuelStationOrders } from '../fuel-station-commands';
import { FuelStationStore } from '../stores/fuel-station-store';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class GetFuelStationOrdersHandler
    extends CommandHandler<GetFuelStationOrders, FuelOrder[]> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId }: GetFuelStationOrders): Observable<FuelOrder[]> {
        return this.api.getFuelStationOrders(fuelStationId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: 'error',
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.fuelOrdersErrorDetail')
                });
                return throwError(() => e);
            }),
            tap(fuelOrders => this.store.fuelOrders = fuelOrders)
        );
    }
}
