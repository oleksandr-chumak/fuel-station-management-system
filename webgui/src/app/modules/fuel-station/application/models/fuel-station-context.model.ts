import { FuelOrder } from '~fuel-order/api/models/fuel-order.model';
import { FuelStation } from '~fuel-station/api/models/fuel-station.model';
import { Manager } from '~manager/api/models/manager.model';

export class FuelStationContext {
  constructor(
    public fuelStation: FuelStation,
    public managers: Manager[],
    public fuelOrders: FuelOrder[]
  ) {}
}