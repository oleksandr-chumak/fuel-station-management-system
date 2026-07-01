import { FuelGrade } from "../core/fuel-grade.enum";

export class FuelTank {
  constructor(
    public id: number,
    public currentVolume: number,
    public pendingVolume: number,
    public maxCapacity: number,
    public fuelGrade: FuelGrade,
    public lastRefillDate: Date | null = null
  ) {}

  get availableVolume(): number {
    return this.maxCapacity - this.currentVolume - this.pendingVolume;
  }

  clone(): FuelTank {
    const clonedLastRefillDate = this.lastRefillDate ? new Date(this.lastRefillDate.getTime()) : null;

    return new FuelTank(
      this.id,
      this.currentVolume,
      this.pendingVolume,
      this.maxCapacity,
      this.fuelGrade,
      clonedLastRefillDate
    );
  }

}
