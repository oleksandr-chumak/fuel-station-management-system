import { Actor } from "../core/actor";
import { DomainEvent } from "../core/domain-event";

export enum FuelOrderEventType {
    FUEL_ORDER_CREATED = "FUEL_ORDER_CREATED",
    FUEL_ORDER_CONFIRMED = "FUEL_ORDER_CONFIRMED",
    FUEL_ORDER_REJECTED = "FUEL_ORDER_REJECTED",
    FUEL_ORDER_PROCESSED = "FUEL_ORDER_PROCESSED"
}

export class FuelOrderEvent extends DomainEvent {
    constructor(
        public fuelOrderId: number, 
        public fuelStationId: number, 
        occurredAt: string, 
        performedBy: Actor
    ) {
        super(occurredAt, performedBy);
    }
}

export class FuelOrderCreated extends FuelOrderEvent {}
export class FuelOrderConfirmed extends FuelOrderEvent {}
export class FuelOrderRejected extends FuelOrderEvent {}
export class FuelOrderProcessed extends FuelOrderEvent {}
