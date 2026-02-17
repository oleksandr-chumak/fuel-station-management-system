import { inject, Injectable } from "@angular/core";
import { CommandHandler } from "../../common/command-handler";
import { RejectFuelStationOrder } from "../fuel-station-commands";
import { FuelOrder } from "../../../../../../fsms-client-sdk/dist/fsms-web-api/types/fsms-web-api";
import { Observable, tap } from "rxjs";
import { RejectFuelOrderHandler } from "../../fuel-orders/handlers/reject-fuel-order-handler";
import { FuelStationStore } from "../fuel-station-store";

@Injectable({ providedIn: "root" })
export class RejectFuelStationOrderHandler 
    extends CommandHandler<RejectFuelStationOrder, FuelOrder> {

    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly confirmFuelOrderHandler = inject(RejectFuelOrderHandler);

    execute(command: RejectFuelStationOrder): Observable<FuelOrder> {
        return this.confirmFuelOrderHandler.handle({ fuelOrderId: command.fuelOrderId })
            .pipe(
                tap((fuelOrder) => {
                    const orders = this.fuelStationStore.fuelOrders
                        .map(order => {
                            if (order.fuelOrderId === fuelOrder.fuelOrderId) {
                                order.reject();
                            }
                            return order;
                        });

                    this.fuelStationStore.fuelOrders = orders;
                })
            )
    }

}