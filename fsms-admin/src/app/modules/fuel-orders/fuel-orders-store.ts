import { BehaviorSubject } from "rxjs";
import { FuelOrder } from "fsms-web-api";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: "root" })
export class FuelOrdersStore {
    private _fuelOrders = new BehaviorSubject<FuelOrder[]>([]);

    get fuelOrders() {
        return this._fuelOrders.getValue();
    }
    
    get fuelOrders$() {
        return this._fuelOrders.asObservable();
    }

    set fuelOrders(fuelOrders: FuelOrder[]) {
        this._fuelOrders.next(fuelOrders);
    }

    reset() {
        this._fuelOrders.next([]);
    }

}