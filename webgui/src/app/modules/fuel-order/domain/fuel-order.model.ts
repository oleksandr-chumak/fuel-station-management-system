import FuelGrade from "../../common/domain/fuel-grade.enum";
import FuelOrderStatus from "./fuel-order-status.enum";

export default class FuelOrder {
    constructor(
        public id: number,
        public status: FuelOrderStatus,
        public grade: FuelGrade,
        public amount: number,
        public fuelStationId: number,
        public createdAt: Date
    ) {}

    confirm() {
        this.status = FuelOrderStatus.Confirmed;
    }

    reject() {
        this.status = FuelOrderStatus.Rejected;
    }
}