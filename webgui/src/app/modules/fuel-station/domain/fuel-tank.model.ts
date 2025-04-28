import FuelGrade from "../../common/domain/fuel-grade.enum";

export class FuelTank {
    constructor(
      public id: number,
      public currentVolume: number,
      public maxCapacity: number,
      public fuelGrade: FuelGrade,
      public lastRefillDate: Date | null = null
    ) {}
  }