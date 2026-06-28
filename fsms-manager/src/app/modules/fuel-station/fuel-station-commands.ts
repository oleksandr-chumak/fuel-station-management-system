import { FuelGrade, FuelOrderAllocation } from "fsms-web-api";

export interface GetAssignedFuelStations {
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
    allocations: FuelOrderAllocation[];
}

export interface DispenseFuel {
    fuelStationId: number;
    fuelTankId: number;
    volume: number;
}

export interface DecommissionFuelTank {
    fuelStationId: number;
    fuelTankId: number;
}

export interface InstallFuelTank {
    fuelStationId: number;
    fuelGrade: FuelGrade;
    maxCapacity: number;
}
