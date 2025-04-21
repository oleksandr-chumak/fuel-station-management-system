import FuelGrade from "../../common/domain/fuel-grade.enum";
import FuelPrice from "./fuel-price.model";

export class FuelTank {
    constructor(
      public id: number,
      public currentVolume: number,
      public maxCapacity: number,
      public fuelGrade: FuelGrade,
      public lastRefillDate: Date | null = null
    ) {}
  }