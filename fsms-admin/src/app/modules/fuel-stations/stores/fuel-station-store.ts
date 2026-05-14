import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { FuelOrder, FuelPurchase, FuelStation, Manager } from "fsms-web-api";

@Injectable({ providedIn: "root" })
export class FuelStationStore {
  private _fuelStation = new BehaviorSubject<FuelStation | null>(null);
  private _managers = new BehaviorSubject<Manager[] | null>(null);
  private _fuelOrders = new BehaviorSubject<FuelOrder[] | null>(null);
  private _fuelPurchases = new BehaviorSubject<FuelPurchase[] | null>(null);

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

  get managers(): Manager[] | null {
    return this._managers.getValue();
  }

  set managers(value: Manager[]) {
    this._managers.next(value);
  }

  get managers$() {
    return this._managers.asObservable();
  }

  get fuelOrders(): FuelOrder[] | null {
    return this._fuelOrders.getValue();
  }

  set fuelOrders(value: FuelOrder[]) {
    this._fuelOrders.next(value);
  }

  get fuelOrders$() {
    return this._fuelOrders.asObservable();
  }

  resetFuelOrders(): void {
    this._fuelOrders.next(null);
  }

  get fuelPurchases(): FuelPurchase[] | null {
    return this._fuelPurchases.getValue();
  }

  set fuelPurchases(value: FuelPurchase[]) {
    this._fuelPurchases.next(value);
  }

  get fuelPurchases$() {
    return this._fuelPurchases.asObservable();
  }

  resetFuelPurchases(): void {
    this._fuelPurchases.next(null);
  }

  resetManagers(): void {
    this._managers.next(null);
  }

  resetFuelStation(): void {
    this._fuelStation.next(null);
  }

  reset(): void {
    this.resetFuelStation();
    this.resetManagers();
    this.resetFuelOrders();
    this.resetFuelPurchases();
  }

}