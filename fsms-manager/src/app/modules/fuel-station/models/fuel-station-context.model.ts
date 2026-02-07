import { FuelOrder, FuelStation, Manager } from "fsms-web-api";

export default class FuelStationContext {
  
  constructor(
    public fuelStation: FuelStation,
    public managers: Manager[],
    public fuelOrders: FuelOrder[]
  ) {}

}