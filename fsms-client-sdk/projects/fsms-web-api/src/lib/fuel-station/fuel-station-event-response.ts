export interface FuelStationEventPerformer {
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
}

export interface FuelStationEventManagerResponse {
    managerId: number;
    firstName: string;
    lastName: string;
}

export interface DomainEventResponse {
    type: string;
    occurredAt: string;
    performedBy: FuelStationEventPerformer | null;
}

export interface FuelPriceChangedEventResponse extends DomainEventResponse {
    fuelGrade: string;
    pricePerLiter: number;
}

export interface ManagerAssignedEventResponse extends DomainEventResponse {
    manager: FuelStationEventManagerResponse;
}

export interface ManagerUnassignedEventResponse extends DomainEventResponse {
    manager: FuelStationEventManagerResponse;
}

export interface FuelOrderDomainEventResponse extends DomainEventResponse {
    fuelOrderId: number;
    fuelStationId: number;
}
