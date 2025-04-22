import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { ApiService } from "../../common/infrastructure/api.service";
import { FuelStation } from "../domain/fuel-station.model";
import { plainToInstance } from "class-transformer";
import FuelGrade from "../../common/domain/fuel-grade.enum";
import FuelOrder from "../../fuel-order/domain/fuel-order.model";
import Manager from "../../manager/domain/manager.model";

@Injectable({ providedIn: "root" })
export default class FuelStationApiService {

    private apiService = inject(ApiService);
    
    getFuelStationOrders(fuelStationId: number): Observable<FuelOrder[]> {
        return this.apiService.get("api/fuel-stations/" + fuelStationId + "fuel-orders");
    }

    getAssignedManagers(fuelStationId: number): Observable<Manager[]> {
        return this.apiService.get("api/fuel-stations/" + fuelStationId + "/managers")
    }

    getFuelStations(): Observable<FuelStation[]> {
        return this.apiService.get("api/fuel-stations/")
            .pipe(map((data) => { console.log(data); return plainToInstance(FuelStation, data as Object[])}));
    }
    
    getFuelStationById(fuelStationId: number): Observable<FuelStation> {
        return this.apiService.get("api/fuel-stations/" + fuelStationId);
    }

    changeFuelPrice(fuelStationId: number, fuelGrade: FuelGrade, newFuelPrice: number): Observable<FuelStation> {
        // TODO: Change change-fuel-price to fuel-prices
        return this.apiService.put("api/fuel-stations/" + fuelStationId + "/change-fuel-price", { fuelGrade, newFuelPrice });
    }

    // TODO Maybe change unassign-manager to managers/{managerId}/unassign
    unassignManager(fuelStationId: number, managerId: number): Observable<FuelStation> {
        return this.apiService.put("api/fuel-stations/" + fuelStationId + "/unassign-manager", { managerId });
    }

    // TODO Maybe change assign-manager to managers/{managerId}/assign
    assignManager(fuelStationId: number, managerId: number): Observable<FuelStation> {
        return this.apiService.put("api/fuel-stations/" + fuelStationId + "/assign-manager", { managerId });
    }

    deactivateFuelStation(fuelStationId: number): Observable<FuelStation> {
        return this.apiService.put("api/fuel-stations/" + fuelStationId + "/deactivate")
    }

    createFuelStation(street: string, buildingNumber: string, city: string, postalCode: string, country: string): Observable<FuelStation> {
        return this.apiService.post("api/fuel-stations/", { street, buildingNumber, city, postalCode, country });
    }

}