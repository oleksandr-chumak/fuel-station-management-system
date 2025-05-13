import { inject, Injectable } from "@angular/core";
import { FuelStation } from "../models/fuel-station.model";
import FuelStationStatus from "../models/fuel-station-status.enum";
import FuelPrice from "../models/fuel-price.model";
import { FuelTank } from "../models/fuel-tank.model";
import { FuelGradeMapper } from "../../common/fuel-grade.mapper";

interface FuelPriceJson {
  fuelGrade: string;
  pricePerLiter: number;
}

interface FuelTankJson {
  id: number;
  capacity: number;
  currentLevel: number;
  fuelGrade: string;
}

interface FuelStationJson {
  id: number;
  street: string;
  buildingNumber: string;
  city: string;
  postalCode: string;
  country: string;
  address: string;
  fuelPrices: FuelPriceJson[];
  fuelTanks: FuelTankJson[];
  assignedManagersIds: number[];
  status: string | number;
}

@Injectable({ providedIn: "root" })
export class FuelStationMapper {

  private fuelGradeMapper = inject(FuelGradeMapper);  

  fromJson(json: unknown): FuelStation {
    if (!this.isFuelStationJson(json)) {
      throw new Error('Invalid FuelStation JSON structure');
    }

    const status = this.parseStatus(json.status);
    const fuelPrices = this.parseFuelPrices(json.fuelPrices);
    const fuelTanks = this.parseFuelTanks(json.fuelTanks);

    return new FuelStation(
      json.id,
      json.street,
      json.buildingNumber,
      json.city,
      json.postalCode,
      json.country,
      json.address,
      fuelPrices,
      fuelTanks,
      json.assignedManagersIds,
      status
    );
  }

  private isFuelStationJson(json: unknown): json is FuelStationJson {
    if (typeof json !== 'object' || json === null) return false;

    const typedJson = json as Partial<FuelStationJson>;
    
    return (
      'id' in json && typeof typedJson.id === 'number' &&
      'street' in json && typeof typedJson.street === 'string' &&
      'buildingNumber' in json && typeof typedJson.buildingNumber === 'string' &&
      'city' in json && typeof typedJson.city === 'string' &&
      'postalCode' in json && typeof typedJson.postalCode === 'string' &&
      'country' in json && typeof typedJson.country === 'string' &&
      'address' in json && typeof typedJson.address === 'string' &&
      'fuelPrices' in json && Array.isArray(typedJson.fuelPrices) &&
      'fuelTanks' in json && Array.isArray(typedJson.fuelTanks) &&
      'assignedManagersIds' in json && Array.isArray(typedJson.assignedManagersIds) &&
      'status' in json && (typeof typedJson.status === 'string')
    );
  }

  private parseStatus(status: string | number): FuelStationStatus {
    if (typeof status === 'string') {
      const enumVal = FuelStationStatus[status as keyof typeof FuelStationStatus];
      if (enumVal === undefined) {
        throw new Error(`Invalid status string: ${status}`);
      }
      return enumVal;
    } 

    throw new Error(`Unsupported status type: ${typeof status}`);
  }

  private parseFuelPrices(jsonPrices: FuelPriceJson[]): FuelPrice[] {
    return jsonPrices.map(priceJson => {
      if (!this.isFuelPriceJson(priceJson)) {
        throw new Error('Invalid FuelPrice JSON structure');
      }

      const fuelGrade = this.fuelGradeMapper.map(priceJson.fuelGrade);
      return new FuelPrice(fuelGrade, priceJson.pricePerLiter);
    });
  }

  private isFuelPriceJson(json: unknown): json is FuelPriceJson {
    if (typeof json !== 'object' || json === null) return false;

    return (
      'fuelGrade' in json && typeof (json as { fuelGrade: unknown }).fuelGrade === 'string' &&
      'pricePerLiter' in json && typeof (json as { pricePerLiter: unknown }).pricePerLiter === 'number'
    );
  }

  private parseFuelTanks(jsonTanks: FuelTankJson[]): FuelTank[] {
    return jsonTanks.map(tankJson => {
      if (!this.isFuelTankJson(tankJson)) {
        throw new Error('Invalid FuelTank JSON structure');
      }

      const fuelGrade = this.fuelGradeMapper.map(tankJson.fuelGrade);
      
      return new FuelTank(
        tankJson.id,
        tankJson.capacity,
        tankJson.currentLevel,
        fuelGrade
      );
    });
  }

  private isFuelTankJson(json: unknown): json is FuelTankJson {
    if (typeof json !== 'object' || json === null) return false;

    return (
      'id' in json && typeof (json as { id: unknown }).id === 'number' &&
      'capacity' in json && typeof (json as { capacity: unknown }).capacity === 'number' &&
      'currentLevel' in json && typeof (json as { currentLevel: unknown }).currentLevel === 'number' &&
      'fuelGrade' in json && typeof (json as { fuelGrade: unknown }).fuelGrade === 'string'
    );
  }
}