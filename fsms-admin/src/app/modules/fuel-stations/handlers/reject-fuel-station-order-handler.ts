import { inject, Injectable } from "@angular/core";
import { CommandHandler } from "../../common/command-handler";
import { RejectFuelStationOrder } from "../fuel-station-commands";
import { FuelOrder, FuelOrderRestClient } from "fsms-web-api";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelStationStore } from "../fuel-station-store";
import { MessageService } from "primeng/api";
import { FuelOrderEventHandler } from "../../fuel-orders/fuel-order-event-handler";

@Injectable({ providedIn: "root" })
export class RejectFuelStationOrderHandler
    extends CommandHandler<RejectFuelStationOrder, FuelOrder> {

    private readonly store = inject(FuelStationStore);
    private readonly api = inject(FuelOrderRestClient);
    private readonly fuelOrderEventHandler = inject(FuelOrderEventHandler);

    private readonly messageService = inject(MessageService);

    execute(command: RejectFuelStationOrder): Observable<FuelOrder> {
        return this.api.rejectFuelOrder(command.fuelOrderId)
            .pipe(
                catchError((e) => {
                    this.messageService.add({ 
                        severity: 'error', 
                        summary: 'Error', 
                        detail: 'Failed to reject fuel order' 
                    });
                    return throwError(() => e);
                }),
                tap((fuelOrder) => {
                    if (this.store.fuelOrders == null) {
                        return;
                    }

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