import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { FuelOrderConfirmed, FuelOrderCreated, FuelOrderEvent, FuelOrderEventType, FuelOrderProcessed, FuelOrderRejected } from "./fuel-order-events";
import { StompClient } from "../core/stomp-client";
import { FuelOrderEventMapper } from "./fuel-order-event-mapper";

interface IFuelOrderStompClient {
    onFuelOrderCreated(): Observable<FuelOrderCreated>
    onFuelOrderConfirmed(fuelOrderId: number): Observable<FuelOrderConfirmed>
    onFuelOrderRejected(fuelOrderId: number): Observable<FuelOrderRejected>
    onFuelOrderProcessed(fuelOrderId: number): Observable<FuelOrderProcessed>
    onAll(fuelStationId: number): Observable<FuelOrderEvent>
}

@Injectable({ providedIn: "root" })
export class FuelOrderStompClient implements IFuelOrderStompClient {

    private readonly stompClient = inject(StompClient);
    private readonly mapper = inject(FuelOrderEventMapper);

    onFuelOrderCreated(): Observable<FuelOrderCreated> {
        return this.stompClient
            .watch({ destination: "/topic/fuel-orders/created" })
            .pipe(map(this.mapper.parseFuelOrderCreated));
    }

    onFuelOrderConfirmed(fuelOrderId: number): Observable<FuelOrderConfirmed> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-orders/${fuelOrderId}/confirmed` })
            .pipe(map(this.mapper.parseFuelOrderConfirmed));
    }

    onFuelOrderRejected(fuelOrderId: number): Observable<FuelOrderRejected> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-orders/${fuelOrderId}/rejected` })
            .pipe(map(this.mapper.parseFuelOrderRejected));
    }

    onFuelOrderProcessed(fuelOrderId: number): Observable<FuelOrderProcessed> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-orders/${fuelOrderId}/processed` })
            .pipe(map(this.mapper.parseFuelOrderProcessed));
    }

    onAll(fuelOrderId: number): Observable<FuelOrderEvent> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-orders/${fuelOrderId}/**` })
            .pipe(map((message) => {
                const json = JSON.parse(message.body);
                const eventType = json.type as FuelOrderEventType;

                switch (eventType) {
                    case FuelOrderEventType.FUEL_ORDER_CREATED:
                        return this.mapper.parseFuelOrderCreated(message);
                    case FuelOrderEventType.FUEL_ORDER_CONFIRMED:
                        return this.mapper.parseFuelOrderConfirmed(message);
                    case FuelOrderEventType.FUEL_ORDER_REJECTED:
                        return this.mapper.parseFuelOrderRejected(message);
                    case FuelOrderEventType.FUEL_ORDER_PROCESSED:
                        return this.mapper.parseFuelOrderProcessed(message);
                    default:
                        const exhaustiveCheck: never = eventType;
                        throw new Error(`Unknown fuel order event type: ${eventType}`);
                }
            }));
    }

}
