import FuelGrade from "../../common/fuel-grade.enum";

export default class FuelPrice {

  constructor(public fuelGrade: FuelGrade, public pricePerLiter: number) {}

  clone(): FuelPrice {
    return new FuelPrice(
      this.fuelGrade,
      this.pricePerLiter
    );
  }

}