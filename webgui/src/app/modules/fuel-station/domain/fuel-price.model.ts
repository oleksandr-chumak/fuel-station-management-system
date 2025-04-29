import { Transform } from "class-transformer";
import FuelGrade from "../../common/domain/fuel-grade.enum";

export default class FuelPrice {

  @Transform(({ value }) => {
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
  pricePerLiter: number;

  constructor(fuelGrade: FuelGrade, pricePerLiter: number) {
    this.fuelGrade = fuelGrade;
    this.pricePerLiter = pricePerLiter;
  }

  clone(): FuelPrice {
    return new FuelPrice(
      this.fuelGrade,
      this.pricePerLiter
    );
  }

}