import { FuelGrade } from "../core/fuel-grade.enum";
import { FuelOrderStatus } from "./fuel-order-status.enum";

export class FuelOrder {

    constructor(
        public fuelOrderId: number, 
        public status: FuelOrderStatus, 
        public fuelGrade: FuelGrade, 
        public amount: number, 
        public fuelStationId: number, 
        public createdAt: Date
    ) {}

    get pending() {
        return this.status === FuelOrderStatus.Pending;
    }

    get confirmed() {
        return this.status === FuelOrderStatus.Confirmed;
    }

    get rejected() {
        return this.status === FuelOrderStatus.Rejected;
    }

    get processed() {
        return this.status === FuelOrderStatus.Processed;
    }

    confirm(): void {
        this.status = FuelOrderStatus.Confirmed;
    }

    reject(): void {
        this.status = FuelOrderStatus.Rejected;
    }

    process(): void {
        this.status = FuelOrderStatus.Processed;
    }
    
}