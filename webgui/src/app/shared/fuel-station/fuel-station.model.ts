import { FuelGrade } from "../api/fuel-grade.model";

export interface FuelTank {
    id: number;
    currentVolume: number;
    maxCapacity: number;
    fuelTanks: FuelTank[];
    fuelPrices: FuelPrice[];
    fuelGrade: FuelGrade;
    lastRefillDate: Date | null;
}

export interface FuelPrice {
    fuelGrade: FuelGrade;
    pricePerLiter: number;
}

export interface FuelStation {
    id: number;
    street: string;
    buildingNumber: string;
    city: string;
    postalCode: string;
    fuelPrices: FuelPrice[];
    fuelTanks: FuelTank[];
    assignedManagersIds: number[];
    country: string;
    active: boolean;
}