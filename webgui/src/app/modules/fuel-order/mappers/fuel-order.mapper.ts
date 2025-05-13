import { inject, Injectable } from "@angular/core";
import FuelOrder from "../models/fuel-order.model";
import FuelOrderStatus from "../models/fuel-order-status.enum";
import { FuelGradeMapper } from "../../common/fuel-grade.mapper";

interface FuelOrderJson {
  id: number;
  status: string | number;
  fuelGrade: string | number;
  amount: number;
  fuelStationId: number;
  createdAt: string;
}

@Injectable({ providedIn: "root" })
export class FuelOrderMapper {

  private fuelGradeMapper = inject(FuelGradeMapper);  

  fromJson(json: unknown): FuelOrder {
    if (!this.isFuelOrderJson(json)) {
      throw new Error('Invalid Fuel Order JSON structure');
    }

    const status = this.parseFuelOrderStatus(json.status);
    const fuelGrade = this.fuelGradeMapper.map(json.fuelGrade);
    const createdAt = new Date(json.createdAt);

    return new FuelOrder(
      json.id,
      status,
      fuelGrade,
      json.amount,
      json.fuelStationId,
      createdAt
    );
  }

  private isFuelOrderJson(json: unknown): json is FuelOrderJson {
    if (typeof json !== 'object' || json === null) return false;

    const typedJson = json as Partial<FuelOrderJson>;
    
    return (
      'id' in json && typeof typedJson.id === 'number' &&
      'status' in json && (typeof typedJson.status === 'string') &&
      'fuelGrade' in json && (typeof typedJson.fuelGrade === 'string') &&
      'amount' in json && typeof typedJson.amount === 'number' &&
      'fuelStationId' in json && typeof typedJson.fuelStationId === 'number' &&
      'createdAt' in json && typeof typedJson.createdAt === 'string'
    );
  }

  private parseFuelOrderStatus(status: string | number): FuelOrderStatus {
    if (typeof status === 'string') {
      switch (status) {
        case "Pending":
          return FuelOrderStatus.Pending;
        case "Confirmed":
          return FuelOrderStatus.Confirmed;
        case "Rejected":
          return FuelOrderStatus.Rejected;
        default:
          throw new Error(`Invalid status string: ${status}`);
      }
    } 

    throw new Error(`Unsupported status type: ${typeof status}`);
  }
  
}