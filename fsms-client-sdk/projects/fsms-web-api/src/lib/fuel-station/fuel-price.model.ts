import { FuelGrade } from "../core/fuel-grade.enum";

export class FuelPrice {

  constructor(public fuelGrade: FuelGrade, public pricePerLiter: number) {}

  clone(): FuelPrice {
    return new FuelPrice(
      this.fuelGrade,
      this.pricePerLiter
    );
  }

}