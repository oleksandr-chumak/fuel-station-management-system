import { inject, Injectable } from "@angular/core";
import { FuelGradeMapper } from "../core/fuel-grade.mapper";
import { FuelStation } from "./fuel-station.model";
import FuelStationStatus from "./fuel-station-status.enum";
import { FuelTank } from "./fuel-tank.model";
import { FuelPrice } from "./fuel-price.model";

@Injectable({ providedIn: "root" })
export class FuelStationMapper {

  private fuelGradeMapper = inject(FuelGradeMapper);  

  fromJson(json: unknown): FuelStation {
    return new FuelStation(
      (json as FuelStation).id,
      (json as FuelStation).street,
      (json as FuelStation).buildingNumber,
      (json as FuelStation).city,
      (json as FuelStation).postalCode,
      (json as FuelStation).country,
      (json as FuelStation).address,
      (json as FuelStation).fuelPrices.map((price) => new FuelPrice(
        price.fuelGrade, 
        price.pricePerLiter
      )),
      (json as FuelStation).fuelTanks.map((tank) => new FuelTank(
        tank.id, 
        tank.currentVolume, 
        tank.maxCapacity, 
        this.fuelGradeMapper.map(tank.fuelGrade), 
        tank.lastRefillDate
      )),
      (json as FuelStation).assignedManagersIds,
      this.parseStatus((json as FuelStation).status)
    );
  }

  private parseStatus(status: string | number): FuelStationStatus {
    if (typeof status === 'string') {
      switch(status) {
        case "active":
          return FuelStationStatus.Active;
        case "deactivated":
          return FuelStationStatus.Deactivated
        default:
          throw new Error(`Unsupported status type: ${typeof status}`);
      }
    } 

    throw new Error(`Unsupported status type: ${typeof status}`);
  }

}