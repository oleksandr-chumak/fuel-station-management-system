import FuelPrice from "./fuel-price.model";
import { FuelTank } from "./fuel-tank.model";

export class FuelStation {
    constructor(
      public id: number,
      public street: string,
      public buildingNumber: string,
      public city: string,
      public postalCode: string,
      public country: string,
      public fuelPrices: FuelPrice[] = [],
      public fuelTanks: FuelTank[] = [],
      public assignedManagersIds: number[] = [],
      public active: boolean = false
    ) {}
  }