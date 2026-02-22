import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelOrder, FuelOrderRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { RejectFuelOrder } from '../fuel-order-commands';
import { MessageService } from 'primeng/api';
import { FuelOrdersStore } from '../fuel-orders-store';
import { FuelOrderEventHandler } from '../fuel-order-event-handler';

@Injectable({ providedIn: 'root' })
export class RejectFuelOrderHandler
    extends CommandHandler<RejectFuelOrder, FuelOrder> {

    private readonly store = inject(FuelOrdersStore);
    private readonly api = inject(FuelOrderRestClient);
    private readonly fuelOrderEventHandler = inject(FuelOrderEventHandler);

    private readonly messageService = inject(MessageService);

    execute({ fuelOrderId }: RejectFuelOrder): Observable<FuelOrder> {
        return this.api.rejectFuelOrder(fuelOrderId).pipe(
            catchError((e) => {
                this.messageService.add({ 
                    severity: 'error', 
                    summary: 'Error', 
                    detail: 'Failed to reject fuel order' 
                });
                return throwError(() => e);
            }),
            tap((fuelOrder) => {
                this.store.fuelOrders = this.fuelOrderEventHandler
                    .handleFuelOrderRejected(fuelOrder.fuelOrderId, this.store.fuelOrders);
                this.messageService.add({
                    severity: 'success',
                    summary: 'Order Rejected',
                    detail: 'The fuel order has been rejected'
                });
            })
        )
    }
}