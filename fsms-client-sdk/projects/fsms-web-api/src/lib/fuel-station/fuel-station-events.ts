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

export class FuelStationEvent {
    constructor(public fuelStationId: number) {}
}

export class FuelStationCreated extends FuelStationEvent {
}  

export class FuelPriceChanged extends FuelStationEvent {
    constructor(fuelStationId: number, public fuelGrade: FuelGrade, public pricePerLiter: number ) {
        super(fuelStationId);
    }
}

export class FuelStationDeactivated extends FuelStationEvent {
    public type = FuelStationEventType.FUEL_STATION_DEACTIVATED;
}

export class ManagerAssignedToFuelStation extends FuelStationEvent {
    constructor(fuelStationId: number, public managerId: number) {
        super(fuelStationId);
    }
}

export class ManagerUnassignedFromFuelStation extends FuelStationEvent {
    constructor(fuelStationId: number, public managerId: number) {
        super(fuelStationId);
    }
}