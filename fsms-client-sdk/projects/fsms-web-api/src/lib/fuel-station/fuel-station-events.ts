import { Actor } from "../core/actor";
import { DomainEvent } from "../core/domain-event";
import { FuelGrade } from "../core/fuel-grade.enum";

export enum FuelStationEventType {
    FUEL_STATION_CREATED = "FUEL_STATION_CREATED",
    FUEL_STATION_DEACTIVATED = "FUEL_STATION_DEACTIVATED",
    FUEL_STATION_FUEL_PRICE_CHANGED = "FUEL_STATION_FUEL_PRICE_CHANGED",
    MANAGER_ASSIGNED_TO_FUEL_STATION = "MANAGER_ASSIGNED_TO_FUEL_STATION",
    MANAGER_UNASSIGNED_FROM_FUEL_STATION = "MANAGER_UNASSIGNED_FROM_FUEL_STATION",
    FUEL_ORDER_CREATED = "FUEL_ORDER_CREATED",
    FUEL_ORDER_CONFIRMED = "FUEL_ORDER_CONFIRMED",
    FUEL_ORDER_REJECTED = "FUEL_ORDER_REJECTED",
    FUEL_ORDER_PROCESSED = "FUEL_ORDER_PROCESSED"
}

export class FuelStationEvent extends DomainEvent {
    constructor(public fuelStationId: number, occurredAt: string, performedBy: Actor) {
        super(occurredAt, performedBy);
      }
}

export class FuelStationCreated extends FuelStationEvent {
}

export class FuelPriceChanged extends FuelStationEvent {
    constructor(
        fuelStationId: number,
        public fuelGrade: FuelGrade,
        public pricePerLiter: number,
        occurredAt: string,
        performedBy: Actor
    ) {
        super(fuelStationId, occurredAt, performedBy);
    }
}

export class FuelStationDeactivated extends FuelStationEvent {
    public type = FuelStationEventType.FUEL_STATION_DEACTIVATED;
}

export class ManagerAssignedToFuelStation extends FuelStationEvent {
    constructor(
        fuelStationId: number,
        public managerId: number,
        occurredAt: string,
        performedBy: Actor
    ) {
        super(fuelStationId, occurredAt, performedBy);
    }
}

export class ManagerUnassignedFromFuelStation extends FuelStationEvent {
    constructor(
        fuelStationId: number,
        public managerId: number,
        occurredAt: string,
        performedBy: Actor
    ) {
        super(fuelStationId, occurredAt, performedBy);
    }
}
