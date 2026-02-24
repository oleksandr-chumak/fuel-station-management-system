import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { FuelStation } from "fsms-web-api";

@Injectable({ providedIn: "root" })
export class AssignedFuelStationsStore {

    private _fuelStations = new BehaviorSubject<FuelStation[]>([]);

    get fuelStations(): FuelStation[] {
        return this._fuelStations.getValue();
    }

    set fuelStations(value: FuelStation[]) {
        this._fuelStations.next(value);
    }

    get fuelStations$() {
        return this._fuelStations.asObservable();
    }

    reset(): void {
        this._fuelStations.next([]);
    }
}
