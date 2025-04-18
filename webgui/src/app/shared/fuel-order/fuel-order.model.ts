import { FuelGrade } from "../api/fuel-grade.model";

export enum FuelOrderStatus {
    Pending,
    Confirmed,
    Rejected
}

export interface FuelOrder {
    id: number;
    status: FuelOrderStatus;
    grade: FuelGrade;
    amount: number;
    fuelStationId: number;
    createdAt: Date;
}