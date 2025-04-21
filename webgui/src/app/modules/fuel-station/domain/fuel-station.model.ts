import { Type } from "class-transformer";
import FuelPrice from "./fuel-price.model";
import FuelStationStatus from "./fuel-station-status.enum";
import { FuelTank } from "./fuel-tank.model";

export class FuelStation {
  id: number;
  street: string;
  buildingNumber: string;
  city: string;
  postalCode: string;
  country: string;
  address: string;
  @Type(() => FuelPrice)
  fuelPrices: FuelPrice[];
  @Type(() => FuelTank)
  fuelTanks: FuelTank[];
  assignedManagersIds: number[];
  status: FuelStationStatus;

  constructor(
    id: number,
    street: string,
    buildingNumber: string,
    city: string,
    postalCode: string,
    country: string,
    address: string,
    fuelPrices: FuelPrice[] = [],
    fuelTanks: FuelTank[] = [],
    assignedManagersIds: number[] = [],
    status: FuelStationStatus
  ) {
    this.id = id;
    this.street = street;
    this.buildingNumber = buildingNumber;
    this.city = city;
    this.postalCode = postalCode;
    this.country = country;
    this.address = address;
    this.fuelPrices = fuelPrices;
    this.fuelTanks = fuelTanks;
    this.assignedManagersIds = assignedManagersIds;
    this.status = status;
  }

  get active() {
    return this.status === FuelStationStatus.Active;
  }
  
  get deactivated() {
    return this.status === FuelStationStatus.Deactivated;
  }
}