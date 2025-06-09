import { FuelOrder } from '../../../fuel-order/api/models/fuel-order.model';
import { Manager } from '../../../manager/api/models/manager.model';
import { FuelStation } from '../../api/models/fuel-station.model';

export class FuelStationContext {
  constructor(
    public fuelStation: FuelStation,
    public managers: Manager[],
    public fuelOrders: FuelOrder[]
  ) {}
}