import { FuelStation } from './fuel-station.model';
import Manager from '../../manager/models/manager.model';
import FuelOrder from '../../fuel-order/models/fuel-order.model';

export default class FuelStationContext {
  
  constructor(
    public fuelStation: FuelStation,
    public managers: Manager[],
    public fuelOrders: FuelOrder[]
  ) {}

}