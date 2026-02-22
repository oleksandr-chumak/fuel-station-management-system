import { FuelGrade } from "fsms-web-api";

export interface GetFuelStations {}

export interface GetFuelStationById {
    fuelStationId: number;
}

export interface GetAssignedManagers {
    fuelStationId: number;
}

export interface GetFuelStationOrders {
    fuelStationId: number;
}

export interface CreateFuelStation {
    street: string,
    buildingNumber: string,
    city: string,
    postalCode: string,
    country: string
}

export interface DeactivateFuelStation {
    fuelStationId: number;
}

export interface AssignManager {
    fuelStationId: number;
    managerId: number;
}

export interface UnassignManager {
    fuelStationId: number;
    managerId: number;
}

export interface ChangeFuelPrice {
    fuelStationId: number;
    fuelGrade: FuelGrade;
    newPrice: number;
}

export interface RejectFuelStationOrder {
    fuelOrderId: number;
}

export interface ConfirmFuelStationOrder {
    fuelOrderId: number;
}
