import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { FuelPriceChanged, FuelStationCreated, FuelStationDeactivated, FuelStationEvent, FuelStationEventType, ManagerAssignedToFuelStation, ManagerUnassignedFromFuelStation } from "./fuel-station-events";
import { StompClient } from "../core/stomp-client";
import { FuelGradeMapper } from "../core/fuel-grade.mapper";
import { IMessage } from "@stomp/rx-stomp";
import { FuelOrderEventMapper } from "../fuel-order/fuel-order-event-mapper";
import { FuelOrderConfirmed, FuelOrderCreated, FuelOrderEvent, FuelOrderProcessed, FuelOrderRejected } from "../../public-api";

interface IFuelStationStompClient {
    onFuelStationCreated(): Observable<FuelStationCreated>
    onFuelPriceChanged(fuelStationId: number): Observable<FuelPriceChanged>
    onFuelStationDeactivated(fuelStationId: number): Observable<FuelStationDeactivated>
    onManagerAssignedToFuelStation(fuelStationId: number): Observable<ManagerAssignedToFuelStation>
    onManagerUnassignedFromFuelStation(fuelStationId: number): Observable<ManagerUnassignedFromFuelStation>
    onFuelOrderCreated(fuelStationId: number): Observable<FuelOrderCreated>
    onFuelOrderConfirmed(fuelStationId: number): Observable<FuelOrderConfirmed>
    onFuelOrderRejected(fuelStationId: number): Observable<FuelOrderRejected>
    onFuelOrderProcessed(fuelStationId: number): Observable<FuelOrderProcessed>
    onAll(fuelStationId: number): Observable<FuelStationEvent>
}

@Injectable({ providedIn: "root" })
export class FuelStationStompClient implements IFuelStationStompClient {

    private readonly stompClient = inject(StompClient);
    private readonly fuelOrderEventMapper = inject(FuelOrderEventMapper);
    private readonly fuelGradeMapper = inject(FuelGradeMapper);

    onFuelStationCreated(): Observable<FuelStationCreated> {
        return this.stompClient
            .watch({ destination: "/topic/fuel-stations/created" })
            .pipe(map(this.parseFuelStationCreated));
    }

    onFuelPriceChanged(fuelStationId: number): Observable<FuelPriceChanged> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-price-changed` })
            .pipe(map(this.parseFuelPriceChanged));
    }

    onFuelStationDeactivated(fuelStationId: number): Observable<FuelStationDeactivated> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/deactivated` })
            .pipe(map(this.parseFuelStationDeactivated));
    }

    onManagerAssignedToFuelStation(fuelStationId: number): Observable<ManagerAssignedToFuelStation> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/manager-assigned` })
            .pipe(map(this.parseManagerAssignedToFuelStation));
    }

    onManagerUnassignedFromFuelStation(fuelStationId: number): Observable<ManagerUnassignedFromFuelStation> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/manager-unassigned` })
            .pipe(map(this.parseManagerUnassignedFromFuelStation));
    }

    onFuelOrderCreated(fuelStationId: number): Observable<FuelOrderCreated> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-order-created` })
            .pipe(map(this.fuelOrderEventMapper.parseFuelOrderCreated));
    }

    onFuelOrderConfirmed(fuelStationId: number): Observable<FuelOrderConfirmed> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-order-confirmed` })
            .pipe(map(this.fuelOrderEventMapper.parseFuelOrderConfirmed));
    }

    onFuelOrderRejected(fuelStationId: number): Observable<FuelOrderRejected> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-order-rejected` })
            .pipe(map(this.fuelOrderEventMapper.parseFuelOrderRejected));
    }

    onFuelOrderProcessed(fuelStationId: number): Observable<FuelOrderProcessed> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-order-processed` })
            .pipe(map(this.fuelOrderEventMapper.parseFuelOrderProcessed));
    }

    onAll(fuelStationId: number): Observable<FuelStationEvent | FuelOrderEvent> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/**` })
            .pipe(map((message) => {
                const json = JSON.parse(message.body);
                const eventType = json.type as FuelStationEventType;

                switch (eventType) {
                    case FuelStationEventType.FUEL_STATION_CREATED:
                        return this.parseFuelStationCreated(message);
                    case FuelStationEventType.FUEL_STATION_DEACTIVATED:
                        return this.parseFuelStationDeactivated(message);
                    case FuelStationEventType.FUEL_STATION_FUEL_PRICE_CHANGED:
                        return this.parseFuelPriceChanged(message);
                    case FuelStationEventType.MANAGER_ASSIGNED_TO_FUEL_STATION:
                        return this.parseManagerAssignedToFuelStation(message);
                    case FuelStationEventType.MANAGER_UNASSIGNED_FROM_FUEL_STATION:
                        return this.parseManagerUnassignedFromFuelStation(message);
                    case FuelStationEventType.FUEL_ORDER_CREATED:
                        return this.fuelOrderEventMapper.parseFuelOrderCreated(message);
                    case FuelStationEventType.FUEL_ORDER_CONFIRMED:
                        return this.fuelOrderEventMapper.parseFuelOrderConfirmed(message);
                    case FuelStationEventType.FUEL_ORDER_REJECTED:
                        return this.fuelOrderEventMapper.parseFuelOrderRejected(message);
                    case FuelStationEventType.FUEL_ORDER_PROCESSED:
                        return this.fuelOrderEventMapper.parseFuelOrderProcessed(message);
                    default:
                        const exhaustiveCheck: never = eventType;
                        throw new Error(`Unknown fuel station event type: ${eventType}`);
                }
            }));
    }

    private parseFuelStationCreated(message: IMessage): FuelStationCreated {
        const json = JSON.parse(message.body);
        return new FuelStationCreated(json.fuelStationId);
    }

    private parseFuelPriceChanged(message: IMessage): FuelPriceChanged {
        const json = JSON.parse(message.body);
        return new FuelPriceChanged(
            json.fuelStationId,
            this.fuelGradeMapper.map(json.fuelGrade),
            json.pricePerLiter
        );
    }

    private parseFuelStationDeactivated(message: IMessage): FuelStationDeactivated {
        const json = JSON.parse(message.body);
        return new FuelStationDeactivated(json.fuelStationId);
    }

    private parseManagerAssignedToFuelStation(message: IMessage): ManagerAssignedToFuelStation {
        const json = JSON.parse(message.body);
        return new ManagerAssignedToFuelStation(json.fuelStationId, json.managerId);
    }

    private parseManagerUnassignedFromFuelStation(message: IMessage): ManagerUnassignedFromFuelStation {
        const json = JSON.parse(message.body);
        return new ManagerUnassignedFromFuelStation(json.fuelStationId, json.managerId);
    }
}