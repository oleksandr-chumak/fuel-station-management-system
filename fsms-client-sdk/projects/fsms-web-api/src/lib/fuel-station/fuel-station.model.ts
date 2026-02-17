import { FuelPrice } from "./fuel-price.model";
import FuelStationStatus from "./fuel-station-status.enum";
import { FuelTank } from "./fuel-tank.model";

export class FuelStation {
  
  constructor(
    public fuelStationId: number,
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
  
  clone(): FuelStation {
    const clonedFuelPrices = this.fuelPrices.map(fuelPrice => fuelPrice.clone());
    const clonedFuelTanks = this.fuelTanks.map(fuelTank => fuelTank.clone())
    const clonedManagerIds = [...this.assignedManagersIds];
    
    return new FuelStation(
      this.fuelStationId,
      this.street,
      this.buildingNumber,
      this.city,
      this.postalCode,
      this.country,
      this.address,
      clonedFuelPrices,
      clonedFuelTanks,
      clonedManagerIds,
      this.status
    );
  }

  assignManger(managerId: number): void {
    this.assignedManagersIds.push(managerId);
  }

  unassignManger(managerId: number): void {
    this.assignedManagersIds = this.assignedManagersIds.filter((id) => id !== managerId);
  }

}