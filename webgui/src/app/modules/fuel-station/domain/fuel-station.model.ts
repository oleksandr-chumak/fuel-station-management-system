import FuelPrice from "./fuel-price.model";
import FuelStationStatus from "./fuel-station-status.enum";
import { FuelTank } from "./fuel-tank.model";

export class FuelStation {

    constructor(
      public id: number,
      public street: string,
      public buildingNumber: string,
      public city: string,
      public postalCode: string,
      public country: string,
      public address: string,
      public fuelPrices: FuelPrice[] = [],
      public fuelTanks: FuelTank[] = [],
      public assignedManagersIds: number[] = [],
      public status: FuelStationStatus
    ) {}

    get active() {
      return this.status === FuelStationStatus.Active;
    }

    get deactivated() {
      return this.status === FuelStationStatus.Deactivated;
    }

}