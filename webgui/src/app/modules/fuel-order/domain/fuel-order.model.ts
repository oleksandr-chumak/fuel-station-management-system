import { Transform } from "class-transformer";
import FuelGrade from "../../common/domain/fuel-grade.enum";
import FuelOrderStatus from "./fuel-order-status.enum";

export default class FuelOrder {
    
    id: number;
    @Transform(({value}) => {
        switch (value) {
            case "Pending":
                return FuelOrderStatus.Pending
            case "Confirmed":
                return FuelOrderStatus.Confirmed
            case "Rejected":
                return FuelOrderStatus.Rejected
            default:
                throw new Error("Cannot transform value: " + value + " to FuelOrderStatus enum")
        }
    })
    status: FuelOrderStatus;
    @Transform(({value}) => {
        switch (value) {
            case "Diesel":
                return FuelGrade.Diesel
            case "RON_95":
                return FuelGrade.RON_95
            case "RON_92":
                return FuelGrade.RON_92
            default:
                throw new Error("Cannot transform value: " + value + " to FuelGrade enum")
        }
    })
    fuelGrade: FuelGrade;
    amount: number;
    fuelStationId: number;
    createdAt: Date;

    constructor(id: number, status: FuelOrderStatus, fuelGrade: FuelGrade, amount: number, fuelStationId: number, createdAt: Date) {
        this.id = id;
        this.status = status;
        this.fuelGrade = fuelGrade;
        this.amount = amount;            
        this.fuelStationId = fuelStationId;
        this.createdAt = createdAt;
    }

    get pending() {
        return this.status === FuelOrderStatus.Pending;
    }

    get confirmed() {
        return this.status === FuelOrderStatus.Confirmed;
    }

    get rejected() {
        return this.status === FuelOrderStatus.Rejected;
    }
    
}