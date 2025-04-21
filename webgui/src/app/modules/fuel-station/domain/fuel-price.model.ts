import FuelGrade from "../../common/domain/fuel-grade.enum";

export default class FuelPrice {
    constructor(
      public fuelGrade: FuelGrade,
      public pricePerLiter: number
    ) {}
}