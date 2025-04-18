import { inject, Injectable } from "@angular/core";
import { ApiService, FuelGrade } from "../api";
import { Observable } from "rxjs";
import { FuelOrder } from "../fuel-order";
import { FuelStation } from "./fuel-station.model";

@Injectable({ providedIn: "root" })
export class FuelStationService {

    private apiService = inject(ApiService);
    
    getFuelStationOrders(fuelStationId: number): Observable<FuelOrder[]> {
        return this.apiService.get("api/fuel-stations/" + fuelStationId + "fuel-orders");
    }

    getAssignedManagers(fuelStationId: number): Observable<FuelOrder[]> {
        return this.apiService.get("api/fuel-stations/" + fuelStationId + "/managers");
    }

    getFuelStations(): Observable<FuelStation[]> {
        return this.apiService.get("api/fuel-stations/");
    }
    
    getFuelStationById(fuelStationId: number): Observable<FuelStation> {
        return this.apiService.get("api/fuel-stations/" + fuelStationId);
    }

    changeFuelPrice(fuelStationId: number, fuelGrade: FuelGrade, newFuelPrice: number): Observable<FuelStation> {
        // TODO: Change change-fuel-price to fuel-prices
        return this.apiService.put("api/fuel-stations/" + fuelStationId + "/change-fuel-price", { fuelGrade, newFuelPrice });
    }

    // TODO Maybe change unassign-manager to managers/{managerId}/unassign
    unassignManager(fuelStationId: number, managerId: number) {
        return this.apiService.put("api/fuel-stations/" + fuelStationId + "/unassign-manager", { managerId });
    }

    // TODO Maybe change assign-manager to managers/{managerId}/assign
    assignManager(fuelStationId: number, managerId: number) {
        return this.apiService.put("api/fuel-stations/" + fuelStationId + "/assign-manager", { managerId });
    }

    deactivateFuelStation(fuelStationId: number) {
        return this.apiService.put("api/fuel-stations/" + fuelStationId + "/deactivate")
    }

    createFuelStation(street: string, buildingNumber: string, city: string, postalCode: string, country: string) {
        return this.apiService.post("api/fuel-stations/", { street, buildingNumber, city, postalCode, country });
    }

}