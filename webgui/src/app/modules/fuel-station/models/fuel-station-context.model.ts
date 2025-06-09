import { FuelOrder } from '../../fuel-order/models/fuel-order.model';
import { Manager } from '../../manager/models/manager.model';

import { FuelStation } from './fuel-station.model';

export class FuelStationContext {
  constructor(
    public fuelStation: FuelStation,
    public managers: Manager[],
    public fuelOrders: FuelOrder[]
  ) {}
}