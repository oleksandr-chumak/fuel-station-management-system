import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelOrder, FuelOrderRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { ConfirmFuelOrder } from '../fuel-order-commands';
import { MessageService } from 'primeng/api';
import { FuelOrdersStore } from '../fuel-orders-store';

@Injectable({ providedIn: 'root' })
export class ConfirmFuelOrderHandler
    extends CommandHandler<ConfirmFuelOrder, FuelOrder> {

    private readonly store = inject(FuelOrdersStore);
    private readonly api = inject(FuelOrderRestClient);

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
                const orders = this.store.fuelOrders
                    .map(order => {
                        if (order.fuelOrderId === fuelOrder.fuelOrderId) {
                            order.confirm();
                        }
                        return order;
                    });

                this.store.fuelOrders = orders;
            })
        );
    }
}