import { Transform } from "class-transformer";
import FuelGrade from "../../common/domain/fuel-grade.enum";

export class FuelTank {

  id: number;
  currentVolume: number;
  maxCapacity: number;
  @Transform(({value}) => {
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
  @Transform(({value}) => value ? new Date(value) : null)
  lastRefillDate: Date | null;

  constructor(id: number, currentVolume: number, maxCapacity: number, fuelGrade: FuelGrade, lastRefillDate: Date | null = null) {
    this.id = id;
    this.currentVolume = currentVolume;
    this.maxCapacity = maxCapacity;
    this.fuelGrade = fuelGrade; 
    this.lastRefillDate = lastRefillDate;
  }

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