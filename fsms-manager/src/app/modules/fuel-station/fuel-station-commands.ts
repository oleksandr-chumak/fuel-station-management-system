import { FuelGrade } from "fsms-web-api";

export interface GetManagerFuelStations {
    managerId: number;
}

export interface GetFuelStationById {
    fuelStationId: number;
}

export interface GetAssignedManagers {
    fuelStationId: number;
}

export interface GetFuelStationOrders {
    fuelStationId: number;
}

export interface CreateFuelOrder {
    fuelStationId: number;
    fuelGrade: FuelGrade;
    amount: number;
}
