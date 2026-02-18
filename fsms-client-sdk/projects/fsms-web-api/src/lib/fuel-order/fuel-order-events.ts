export enum FuelOrderEventType {
    FUEL_ORDER_CREATED = "FUEL_ORDER_CREATED",
    FUEL_ORDER_CONFIRMED = "FUEL_ORDER_CONFIRMED",
    FUEL_ORDER_REJECTED = "FUEL_ORDER_REJECTED",
    FUEL_ORDER_PROCESSED = "FUEL_ORDER_PROCESSED"
}

export class FuelOrderEvent {
    constructor(public fuelOrderId: number, public fuelStationId: number) {}
}

export class FuelOrderCreated extends FuelOrderEvent {}
export class FuelOrderConfirmed extends FuelOrderEvent {}
export class FuelOrderRejected extends FuelOrderEvent {}
export class FuelOrderProcessed extends FuelOrderEvent {}
