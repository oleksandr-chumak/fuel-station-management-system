import { CurrencyCode } from "../core/currency";
import { FuelGrade } from "../core/fuel-grade.enum";

export class FuelStationFuelPrice {

  constructor(public fuelGrade: FuelGrade, public pricePerLiter: number, public currency: CurrencyCode) {}

  clone(): FuelStationFuelPrice {
    return new FuelStationFuelPrice(
      this.fuelGrade,
      this.pricePerLiter,
      this.currency
    );
  }

}