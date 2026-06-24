import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { FuelPriceChanged, FuelPurchaseCreated, FuelSaleCreated, FuelStationCreated, FuelStationDeactivated, FuelStationEvent, FuelStationEventType, FuelTankDecommissioned, FuelTankInstalled, ManagerAssignedToFuelStation, ManagerUnassignedFromFuelStation } from "./fuel-station-events";
import { StompClient } from "../core/stomp-client";
import { FuelGradeMapper } from "../core/fuel-grade.mapper";
import { IMessage } from "@stomp/rx-stomp";
import { FuelOrderEventMapper } from "../fuel-order/fuel-order-event-mapper";
import { FuelOrderConfirmed, FuelOrderCreated, FuelOrderProcessed, FuelOrderRejected } from "../../public-api";
import { Actor } from "../core/actor";

interface IFuelStationStompClient {
    onFuelStationCreated(): Observable<FuelStationCreated>
    onFuelPriceChanged(fuelStationId: number): Observable<FuelPriceChanged>
    onFuelStationDeactivated(fuelStationId: number): Observable<FuelStationDeactivated>
    onManagerAssignedToFuelStation(fuelStationId: number): Observable<ManagerAssignedToFuelStation>
    onManagerUnassignedFromFuelStation(fuelStationId: number): Observable<ManagerUnassignedFromFuelStation>
    onFuelTankDecommissioned(fuelStationId: number): Observable<FuelTankDecommissioned>
    onFuelTankInstalled(fuelStationId: number): Observable<FuelTankInstalled>
    onFuelOrderCreated(fuelStationId: number): Observable<FuelOrderCreated>
    onFuelOrderConfirmed(fuelStationId: number): Observable<FuelOrderConfirmed>
    onFuelOrderRejected(fuelStationId: number): Observable<FuelOrderRejected>
    onFuelOrderProcessed(fuelStationId: number): Observable<FuelOrderProcessed>
    onFuelPurchaseCreated(fuelStationId: number): Observable<FuelPurchaseCreated>
    onFuelSaleCreated(fuelStationId: number): Observable<FuelSaleCreated>
    onFuelStationAll(fuelStationId: number): Observable<FuelStationEvent>
    onAll(): Observable<FuelStationEvent>
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

    onFuelTankDecommissioned(fuelStationId: number): Observable<FuelTankDecommissioned> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-tank-decommissioned` })
            .pipe(map(this.parseFuelTankDecommissioned));
    }

    onFuelTankInstalled(fuelStationId: number): Observable<FuelTankInstalled> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-tank-installed` })
            .pipe(map(this.parseFuelTankInstalled.bind(this)));
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

    onFuelPurchaseCreated(fuelStationId: number): Observable<FuelPurchaseCreated> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-purchases` })
            .pipe(map(this.parseFuelPurchaseCreated.bind(this)));
    }

    onFuelSaleCreated(fuelStationId: number): Observable<FuelSaleCreated> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/fuel-sales` })
            .pipe(map(this.parseFuelSaleCreated.bind(this)));
    }

    onFuelStationAll(fuelStationId: number): Observable<FuelStationEvent> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/${fuelStationId}/**` })
            .pipe(map((message) => this.parseEvent(message)));
    }

    onAll(): Observable<FuelStationEvent> {
        return this.stompClient
            .watch({ destination: `/topic/fuel-stations/**` })
            .pipe(map((message) => this.parseEvent(message)));
    }

    private parseEvent(message: IMessage): FuelStationEvent {
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
            case FuelStationEventType.FUEL_TANK_DECOMMISSIONED:
                return this.parseFuelTankDecommissioned(message);
            case FuelStationEventType.FUEL_TANK_INSTALLED:
                return this.parseFuelTankInstalled(message);
            case FuelStationEventType.FUEL_ORDER_CREATED:
                return this.fuelOrderEventMapper.parseFuelOrderCreated(message);
            case FuelStationEventType.FUEL_ORDER_CONFIRMED:
                return this.fuelOrderEventMapper.parseFuelOrderConfirmed(message);
            case FuelStationEventType.FUEL_ORDER_REJECTED:
                return this.fuelOrderEventMapper.parseFuelOrderRejected(message);
            case FuelStationEventType.FUEL_ORDER_PROCESSED:
                return this.fuelOrderEventMapper.parseFuelOrderProcessed(message);
            case FuelStationEventType.FUEL_PURCHASE_CREATED:
                return this.parseFuelPurchaseCreated(message);
            case FuelStationEventType.FUEL_SALE_CREATED:
                return this.parseFuelSaleCreated(message);
            default:
                const exhaustiveCheck: never = eventType;
                throw new Error(`Unknown fuel station event type: ${exhaustiveCheck}`);
        }
    }

    private parseFuelStationCreated(message: IMessage): FuelStationCreated {
        const json = JSON.parse(message.body);
        return new FuelStationCreated(
            json.fuelStationId, 
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }

    private parseFuelPriceChanged(message: IMessage): FuelPriceChanged {
        const json = JSON.parse(message.body);
        return new FuelPriceChanged(
            json.fuelStationId,
            this.fuelGradeMapper.map(json.fuelGrade),
            json.pricePerLiter,
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }

    private parseFuelStationDeactivated(message: IMessage): FuelStationDeactivated {
        const json = JSON.parse(message.body);
        return new FuelStationDeactivated(
            json.fuelStationId,
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }

    private parseManagerAssignedToFuelStation(message: IMessage): ManagerAssignedToFuelStation {
        const json = JSON.parse(message.body);
        return new ManagerAssignedToFuelStation(
            json.fuelStationId, 
            json.managerId,
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }

    private parseManagerUnassignedFromFuelStation(message: IMessage): ManagerUnassignedFromFuelStation {
        const json = JSON.parse(message.body);
        return new ManagerUnassignedFromFuelStation(
            json.fuelStationId,
            json.managerId,
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }

    private parseFuelPurchaseCreated(message: IMessage): FuelPurchaseCreated {
        const json = JSON.parse(message.body);
        return new FuelPurchaseCreated(
            json.fuelStationId,
            json.fuelPurchaseId,
            json.fuelOrderId,
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }

    private parseFuelSaleCreated(message: IMessage): FuelSaleCreated {
        const json = JSON.parse(message.body);
        return new FuelSaleCreated(
            json.fuelStationId,
            json.fuelSaleId,
            json.fuelTankId,
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }

    private parseFuelTankDecommissioned(message: IMessage): FuelTankDecommissioned {
        const json = JSON.parse(message.body);
        return new FuelTankDecommissioned(
            json.fuelStationId,
            json.fuelTankId,
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }

    private parseFuelTankInstalled(message: IMessage): FuelTankInstalled {
        const json = JSON.parse(message.body);
        return new FuelTankInstalled(
            json.fuelStationId,
            json.fuelTankId,
            this.fuelGradeMapper.map(json.fuelGrade),
            Number(json.maxCapacity),
            json.occurredAt,
            new Actor(json.performedBy.id, json.performedBy.type)
        );
    }
}