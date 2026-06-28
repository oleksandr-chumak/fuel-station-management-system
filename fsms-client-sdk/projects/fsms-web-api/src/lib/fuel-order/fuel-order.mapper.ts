import { inject, Injectable } from "@angular/core";
import { FuelOrder } from "./fuel-order.model";
import { FuelOrderAllocation } from "./fuel-order-allocation.model";
import { FuelOrderStatus } from "./fuel-order-status.enum";
import { FuelGradeMapper } from "../core/fuel-grade.mapper";

interface AllocationJson {
  fuelTankId: number;
  volume: number;
}

interface FuelOrderJson {
  fuelOrderId: number;
  status: string;
  fuelGrade: string;
  volume: number;
  allocations: AllocationJson[];
  fuelStationId: number;
  createdAt: string;
}

@Injectable({ providedIn: "root" })
export class FuelOrderMapper {

  private fuelGradeMapper = inject(FuelGradeMapper);

  fromJson(json: unknown): FuelOrder {
    const data = json as FuelOrderJson;
    const status = this.parseFuelOrderStatus(data.status);
    const fuelGrade = this.fuelGradeMapper.map(data.fuelGrade);
    const createdAt = new Date(data.createdAt);
    const allocations = (data.allocations ?? [])
      .map(a => new FuelOrderAllocation(a.fuelTankId, a.volume));

    return new FuelOrder(
      data.fuelOrderId,
      status,
      fuelGrade,
      data.volume,
      allocations,
      data.fuelStationId,
      createdAt
    );
  }

  private parseFuelOrderStatus(status: string | number): FuelOrderStatus {
    if (typeof status === 'string') {
      switch (status) {
        case "pending":
          return FuelOrderStatus.Pending;
        case "confirmed":
          return FuelOrderStatus.Confirmed;
        case "rejected":
          return FuelOrderStatus.Rejected;
        case "processed":
          return FuelOrderStatus.Processed;
        default:
          throw new Error(`Invalid status string: ${status}`);
      }
    }

    throw new Error(`Unsupported status type: ${typeof status}`);
  }

}
