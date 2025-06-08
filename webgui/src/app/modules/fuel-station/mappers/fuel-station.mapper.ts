import { FuelStation } from "../models/fuel-station.model";
import FuelStationStatus from "../models/fuel-station-status.enum";
import FuelPrice from "../models/fuel-price.model";
import { FuelTank } from "../models/fuel-tank.model";
import { FuelGradeMapper } from "../../common/fuel-grade.mapper";

export class FuelStationMapper {
  static fromJson(json: unknown): FuelStation {
    const typedJson = json as Record<string, unknown>
    const status = this.parseStatus(typedJson['status']);
    const fuelPrices = this.parseFuelPrices(typedJson['fuelPrices'] as Record<string,unknown>[]);
    const fuelTanks = this.parseFuelTanks(typedJson['fuelTanks'] as Record<string,unknown>[] );

    return new FuelStation(
      Number(typedJson['id']),
      typedJson['street'] as string,
      typedJson['buildingNumber'] as string,
      typedJson['city'] as string,
      typedJson['postalCode'] as string,
      typedJson['country'] as string,
      typedJson['address'] as string,
      fuelPrices,
      fuelTanks,
      typedJson['assignedManagersIds'] as number[],
      status
    );
  }

  private static parseStatus(status: unknown): FuelStationStatus {
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

  private static parseFuelPrices(data: Record<string, unknown>[]): FuelPrice[] {
    return data.map(json => {
      const fuelGrade = FuelGradeMapper.map(json['fuelGrade']);
      return new FuelPrice(fuelGrade, Number(json['pricePerLiter']));
    });
  }

  private static parseFuelTanks(data: Record<string, unknown>[]): FuelTank[] {
    return data.map(json => {
      const fuelGrade = FuelGradeMapper.map(json['fuelGrade']);
      return new FuelTank(
        Number(json['id']),
        Number(json['currentVolume']),
        Number(json['maxCapacity']),
        fuelGrade,
        json['lastRefillDate'] ? new Date(json['lastRefillDate'] as string) : null
      );
    });
  }

}