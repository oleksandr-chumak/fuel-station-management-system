import { User } from "../auth/user.model";
import { Manager } from "../manager/manager.model";


export interface DomainEventResponse {
    type: string;
    occurredAt: string;
    performedBy: User | null;
}

export interface FuelPriceChangedEventResponse extends DomainEventResponse {
    fuelGrade: string;
    pricePerLiter: number;
}

export interface ManagerAssignedEventResponse extends DomainEventResponse {
    manager: Manager;
}

export interface ManagerUnassignedEventResponse extends DomainEventResponse {
    manager: Manager;
}

export interface FuelOrderDomainEventResponse extends DomainEventResponse {
    fuelOrderId: number;
    fuelStationId: number;
}
