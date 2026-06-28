import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelOrder, FuelOrderRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { RejectFuelOrder } from '../fuel-order-commands';
import { MessageService } from 'primeng/api';
import { FuelOrdersStore } from '../fuel-orders-store';
import { FuelOrderEventHandler } from '../fuel-order-event-handler';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class RejectFuelOrderHandler
    extends CommandHandler<RejectFuelOrder, FuelOrder> {

    private readonly store = inject(FuelOrdersStore);
    private readonly api = inject(FuelOrderRestClient);
    private readonly fuelOrderEventHandler = inject(FuelOrderEventHandler);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelOrderId }: RejectFuelOrder): Observable<FuelOrder> {
        return this.api.rejectFuelOrder(fuelOrderId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: 'error',
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.rejectFuelOrder.errorDetail')
                });
                return throwError(() => e);
            }),
            tap((fuelOrder) => {
                this.store.fuelOrders = this.fuelOrderEventHandler
                    .handleFuelOrderRejected(fuelOrder.fuelOrderId, this.store.fuelOrders);
                this.messageService.add({
                    severity: 'success',
                    summary: this.translate.instant('toasts.rejectFuelOrder.successSummary'),
                    detail: this.translate.instant('toasts.rejectFuelOrder.successDetail')
                });
            })
        )
    }
}
