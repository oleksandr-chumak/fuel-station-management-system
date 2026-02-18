import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { FuelOrder, FuelStation, Manager } from "fsms-web-api";

@Injectable({ providedIn: "root" })
export class FuelStationStore {
  private _fuelStation = new BehaviorSubject<FuelStation | null>(null);
  private _managers = new BehaviorSubject<Manager[]>([]);
  private _fuelOrders = new BehaviorSubject<FuelOrder[]>([]);

  get fuelStation(): FuelStation {
    const value = this._fuelStation.getValue();
    if (value === null) throw new Error("FuelStation is not set");
    return value;
  }

  set fuelStation(value: FuelStation) {
    if (value === null || value === undefined) throw new Error("FuelStation cannot be null or undefined");
    this._fuelStation.next(value);
  }

  get fuelStation$() {
    return this._fuelStation.asObservable();
  }

  get managers(): Manager[] {
    return this._managers.getValue();
  }

  set managers(value: Manager[]) {
    this._managers.next(value);
  }

  get managers$() {
    return this._managers.asObservable();
  }

  get fuelOrders(): FuelOrder[] {
    return this._fuelOrders.getValue();
  }

  set fuelOrders(value: FuelOrder[]) {
    this._fuelOrders.next(value);
  }

  get fuelOrders$() {
    return this._fuelOrders.asObservable();
  }

  reset(): void {
    this._fuelStation.next(null);
    this._managers.next([]);
    this._fuelOrders.next([]);
  }
}
