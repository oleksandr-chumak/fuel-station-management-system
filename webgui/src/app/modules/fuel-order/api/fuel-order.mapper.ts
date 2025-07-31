import { FuelOrderStatus } from './models/fuel-order-status.enum';
import { FuelOrder } from './models/fuel-order.model';

import { FuelGradeMapper } from '~common/api/fuel-grade.mapper';

export class FuelOrderMapper {
  static fromJson(json: unknown): FuelOrder {
    const typedJson = json as Record<string, unknown>;
    const status = this.parseFuelOrderStatus(typedJson['status']);
    const fuelGrade = FuelGradeMapper.map(typedJson['fuelGrade']);
    const createdAt = new Date(typedJson['createdAt'] as string);

    return new FuelOrder(
      Number(typedJson['id']),
      status,
      fuelGrade,
      Number(typedJson['amount']),
      Number(typedJson['fuelStationId']),
      createdAt
    );
  }

  private static parseFuelOrderStatus(status: unknown): FuelOrderStatus {
    if (typeof status === 'string') {
      switch (status) {
      case 'pending':
        return FuelOrderStatus.Pending;
      case 'confirmed':
        return FuelOrderStatus.Confirmed;
      case 'rejected':
        return FuelOrderStatus.Rejected;
      default:
        throw new Error(`Invalid status string: ${status}`);
      }
    } 

    throw new Error(`Unsupported status type: ${typeof status}`);
  }
}