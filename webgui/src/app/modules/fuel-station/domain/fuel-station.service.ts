import { inject, Injectable } from "@angular/core";
import { FuelStation } from "./fuel-station.model";
import FuelPrice from "./fuel-price.model";
import FuelStationApiService from "../infrastructure/fuel-station-api.service";
import { Observable } from "rxjs";

@Injectable({ providedIn: "root" })
export default class FuelStationService {

    private fuelStationApiService: FuelStationApiService = inject(FuelStationApiService);

    changeFuelPrice(fuelStation: FuelStation, fuelPrice: FuelPrice, newFuelPrice: number): Observable<FuelStation> {
        return this.fuelStationApiService.changeFuelPrice(fuelStation.id, fuelPrice.fuelGrade, newFuelPrice); 
    }

    unassignManager(fuelStation: FuelStation, managerId: number): Observable<FuelStation> {

    }

    assignManager(fuelStation: FuelStation, managerId: number): Observable<FuelStation> {
    }

    deactivateFuelStation(fuelStation: FuelStation): Observable<FuelStation> {
    }

}