import { inject, Injectable } from "@angular/core";
import { FuelOrder } from "./fuel-order.model";
import { FuelOrderStatus } from "./fuel-order-status.enum";
import { FuelGradeMapper } from "../core/fuel-grade.mapper";

@Injectable({ providedIn: "root" })
export class FuelOrderMapper {

  private fuelGradeMapper = inject(FuelGradeMapper);  

  fromJson(json: unknown): FuelOrder {
    const status = this.parseFuelOrderStatus((json as FuelOrder).status);
    const fuelGrade = this.fuelGradeMapper.map((json as FuelOrder).fuelGrade);
    const createdAt = new Date((json as FuelOrder).createdAt);

    return new FuelOrder(
      (json as FuelOrder).fuelOrderId,
      status,
      fuelGrade,
      (json as FuelOrder).amount,
      (json as FuelOrder).fuelStationId,
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
        default:
          throw new Error(`Invalid status string: ${status}`);
      }
    } 

    throw new Error(`Unsupported status type: ${typeof status}`);
  }
  
}