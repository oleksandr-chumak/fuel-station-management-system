import { FuelGrade } from '../../common/fuel-grade.enum';

export class FuelTank {
  constructor(
    public id: number, 
    public currentVolume: number,
    public maxCapacity: number, 
    public fuelGrade: FuelGrade, 
    public lastRefillDate: Date | null = null
  ) {}

  clone(): FuelTank {
    const clonedLastRefillDate = this.lastRefillDate ? new Date(this.lastRefillDate.getTime()) : null;
    
    return new FuelTank(
      this.id,
      this.currentVolume,
      this.maxCapacity,
      this.fuelGrade,
      clonedLastRefillDate
    );
  }
}