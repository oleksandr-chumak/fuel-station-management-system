import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { FuelDeliveryProcesed, FuelPriceChanged, FuelStationCreated, FuelStationDeactivated, FuelStationEvent, FuelStationEventType, ManagerAssignedToFuelStation, ManagerUnassignedFromFuelStation } from "./fuel-station-events";
import { StompClient } from "../core/stomp-client";
import { FuelGradeMapper } from "../core/fuel-grade.mapper";
import { IMessage } from "@stomp/rx-stomp";

interface IFuelStationStompClient {
    onFuelStationCreated(): Observable<FuelStationCreated>
    onFuelDeliveryProcessed(fuelStationId: number): Observable<FuelDeliveryProcesed>
    onFuelPriceChanged(fuelStationId: number): Observable<FuelPriceChanged>
    onFuelStationDeactivated(fuelStationId: number): Observable<FuelStationDeactivated>
    onManagerAssingedToFuelStation(fuelStationId: number): Observable<ManagerAssignedToFuelStation>
    onManagerUnassignedFromFuelStation(fuelStationId: number): Observable<ManagerUnassignedFromFuelStation>
    onAll(fuelStationId: number): Observable<FuelStationEvent>
}

@Injectable({ providedIn: "root" })
export class FuelStationStompClient implements IFuelStationStompClient {

    private stompClient = inject(StompClient);
    private fuelGradeMapper = inject(FuelGradeMapper);

    onFuelStationCreated(): Observable<FuelStationCreated> {
        return this.stompClient
            .watch({ destination: "/topic/fuel-stations/created" })
            .pipe(map(this.parseFuelStationCreated));
    }

    onFuelDeliveryProcessed(fuelStationId: number): Observable<FuelDeliveryProcesed> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-delivery-processed` })
            .pipe(map(this.parseFuelDeliveryProcessed));
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

    onManagerAssingedToFuelStation(fuelStationId: number): Observable<ManagerAssignedToFuelStation> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/manager-assigned` })
            .pipe(map(this.parseManagerAssingedToFuelStation));
    }

    onManagerUnassignedFromFuelStation(fuelStationId: number): Observable<ManagerUnassignedFromFuelStation> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/manager-unassigned` })
            .pipe(map(this.parseManagerUnassignedFromFuelStation));
    }

    onAll(fuelStationId: number): Observable<FuelStationEvent> {
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
                    case FuelStationEventType.FUEL_STATION_FUEL_DELIVERY_PROCESSED:
                        return this.parseFuelDeliveryProcessed(message);
                    case FuelStationEventType.MANAGER_ASSIGNED_TO_FUEL_STATION:
                        return this.parseManagerAssingedToFuelStation(message);
                    case FuelStationEventType.MANAGER_UNASSIGNED_FROM_FUEL_STATION:
                        return this.parseManagerUnassignedFromFuelStation(message);
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

    private parseFuelDeliveryProcessed(message: IMessage): FuelDeliveryProcesed {
        const json = JSON.parse(message.body);
        return new FuelDeliveryProcesed(json.fuelStationId);
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

    private parseManagerAssingedToFuelStation(message: IMessage): ManagerAssignedToFuelStation {
        const json = JSON.parse(message.body);
        return new ManagerAssignedToFuelStation(json.fuelStationId, json.managerId);
    }

    private parseManagerUnassignedFromFuelStation(message: IMessage): ManagerUnassignedFromFuelStation {
        const json = JSON.parse(message.body);
        return new ManagerUnassignedFromFuelStation(json.fuelStationId, json.managerId);
    }
}