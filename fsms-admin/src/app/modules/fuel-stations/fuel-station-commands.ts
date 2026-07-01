import { CountryCode, FuelGrade } from "fsms-web-api";

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
    country: CountryCode
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

export interface UpdateFuelPrices {
    fuelStationId: number;
    prices: { fuelGrade: FuelGrade, newPrice: number }[];
}

export interface RejectFuelStationOrder {
    fuelOrderId: number;
}

export interface ConfirmFuelStationOrder {
    fuelOrderId: number;
    pricePerLiter: number;
}

export interface GetFuelStationEvents {
    fuelStationId: number;
    occurredAfter?: string;
}

export interface GetFuelStationFuelPurchases {
    fuelStationId: number;
}

export interface GetFuelStationFuelSales {
    fuelStationId: number;
}

export interface GetFuelPriceHistory {
    fuelStationId: number;
    from?: string;
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
