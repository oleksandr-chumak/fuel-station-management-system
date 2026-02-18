import { inject, Injectable } from "@angular/core"
import { CommandHandler } from "../../common/command-handler";
import { ConfirmFuelStationOrder } from "../fuel-station-commands";
import { FuelOrder } from "../../../../../../fsms-client-sdk/dist/fsms-web-api/types/fsms-web-api";
import { Observable, tap } from "rxjs";
import { ConfirmFuelOrderHandler } from "../../fuel-orders/handlers/confirm-fuel-order-handler";
import { FuelStationStore } from "../fuel-station-store";
import { MessageService } from "primeng/api";


@Injectable({ providedIn: "root" })
export class ConfirmFuelStationOrderHandler
    extends CommandHandler<ConfirmFuelStationOrder, FuelOrder> {

    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly confirmFuelOrderHandler = inject(ConfirmFuelOrderHandler);

    private readonly messageService = inject(MessageService);

    execute(command: ConfirmFuelStationOrder): Observable<FuelOrder> {
        return this.confirmFuelOrderHandler.handle({ fuelOrderId: command.fuelOrderId })
            .pipe(
                tap((fuelOrder) => {
                    const orders = this.fuelStationStore.fuelOrders
                        .map(order => {
                            if (order.fuelOrderId === fuelOrder.fuelOrderId) {
                                order.confirm();
                            }
                            return order;
                        });
                    this.fuelStationStore.fuelOrders = orders;

                    this.messageService.add({
                        severity: 'success', 
                        summary: 'Order Confirmed', 
                        detail: 'The fuel order has been confirmed'  
                    });
                })
            )
    }

}