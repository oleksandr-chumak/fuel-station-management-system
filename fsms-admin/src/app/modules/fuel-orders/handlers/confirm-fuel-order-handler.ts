import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelOrder, FuelOrderRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { ConfirmFuelOrder } from '../fuel-order-commands';
import { MessageService } from 'primeng/api';
import { FuelOrdersStore } from '../fuel-orders-store';
import { FuelOrderEventHandler } from '../fuel-order-event-handler';

@Injectable({ providedIn: 'root' })
export class ConfirmFuelOrderHandler
    extends CommandHandler<ConfirmFuelOrder, FuelOrder> {

    private readonly store = inject(FuelOrdersStore);
    private readonly api = inject(FuelOrderRestClient);
    private readonly fuelOrderEventHandler = inject(FuelOrderEventHandler);

    private readonly messageService = inject(MessageService);

    execute({ fuelOrderId }: ConfirmFuelOrder): Observable<FuelOrder> {
        return this.api.confirmFuelOrder(fuelOrderId).pipe(
            catchError((e) => {
                this.messageService.add({ 
                    severity: 'error', 
                    summary: 'Error', 
                    detail: 'Failed to confirm fuel order' 
                });
                return throwError(() => e);
            }),
            tap((fuelOrder) => {
                this.messageService.add({
                    severity: 'success', 
                    summary: 'Order Confirmed', 
                    detail: 'The fuel order has been confirmed'  
                });
                this.store.fuelOrders = this.fuelOrderEventHandler
                    .handleFuelOrderConfirmed(fuelOrder.fuelOrderId, this.store.fuelOrders);
            })
        );
    }
}