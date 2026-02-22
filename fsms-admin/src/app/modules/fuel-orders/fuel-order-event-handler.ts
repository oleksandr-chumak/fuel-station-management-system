import { inject, Injectable } from "@angular/core";
import { FuelOrdersStore } from "./fuel-orders-store";
import { FuelOrder, FuelOrderConfirmed, FuelOrderCreated, FuelOrderEvent, FuelOrderProcessed, FuelOrderRejected, FuelOrderRestClient, FuelOrderStompClient } from "fsms-web-api";
import { map, Observable, tap } from "rxjs";


@Injectable({ providedIn: "root" })
export class FuelOrderEventHandler {

    private store = inject(FuelOrdersStore);
    private fuelOrderStompClient = inject(FuelOrderStompClient);
    private fuelOrderRestClient = inject(FuelOrderRestClient);

    start(): Observable<FuelOrderEvent> {
        return this.fuelOrderStompClient.onAll()
            .pipe(
                tap((event) => {
                    if (event instanceof FuelOrderCreated) {
                        this.handleFuelOrderCreated(event.fuelOrderId, this.store.fuelOrders)
                            .pipe(tap((fuelOrders) => this.store.fuelOrders = fuelOrders))
                            .subscribe();
                    } else if (event instanceof FuelOrderConfirmed) {
                        this.store.fuelOrders = this.handleFuelOrderConfirmed(event.fuelOrderId, this.store.fuelOrders);
                    } else if (event instanceof FuelOrderRejected) {
                        this.store.fuelOrders = this.handleFuelOrderRejected(event.fuelOrderId, this.store.fuelOrders);
                    } else if (event instanceof FuelOrderProcessed) {
                        this.store.fuelOrders = this.handleFuelOrderProcessed(event.fuelOrderId, this.store.fuelOrders);
                    }
                })
            )

    }

    handleFuelOrderCreated(fuelOrderId: number, fuelOrders: FuelOrder[]): Observable<FuelOrder[]> {
        return this.fuelOrderRestClient.getFuelOrderById(fuelOrderId)
            .pipe(map((fuelOrder) => [fuelOrder, ...fuelOrders]));
    }

    handleFuelOrderConfirmed(fuelOrderId: number, fuelOrders: FuelOrder[]): FuelOrder[] {
        return this.updateFuelOrder(fuelOrderId, fuelOrders, (order) => {
            if(!order.processed) {
                order.confirm()
            }
        });
    }

    handleFuelOrderRejected(fuelOrderId: number, fuelOrders: FuelOrder[]): FuelOrder[] {
        return this.updateFuelOrder(fuelOrderId, fuelOrders, (order) => order.reject());
    }

    handleFuelOrderProcessed(fuelOrderId: number, fuelOrders: FuelOrder[]): FuelOrder[] {
        return this.updateFuelOrder(fuelOrderId, fuelOrders, (order) => order.process());
    }

    private updateFuelOrder(fuelOrderId: number, fuelOrders: FuelOrder[], cb: (order: FuelOrder) => void): FuelOrder[] {
        return fuelOrders.map((fuelOrder) => {
            if (fuelOrder.fuelOrderId === fuelOrderId) {
                cb(fuelOrder);
            }
            return fuelOrder;
        });
    }

}

