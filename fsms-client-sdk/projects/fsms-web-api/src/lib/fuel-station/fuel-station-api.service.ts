import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { ApiService } from "../core/web-api.service";
import { FuelStationMapper } from "./fuel-station.mapper";
import { ManagerMapper } from "../manager/manager.mapper";
import { FuelOrderMapper } from "../fuel-order/fuel-order.mapper";
import { FuelOrder } from "../fuel-order/fuel-order.model";
import { Manager } from "../manager/manager.model";
import { FuelStation } from "./fuel-station.model";
import { FuelGrade } from "../core/fuel-grade.enum";

@Injectable({ providedIn: "root" })
export class FuelStationApiService {
    
    private apiService = inject(ApiService);
    private fuelStationMapper = inject(FuelStationMapper);
    private managerMapper = inject(ManagerMapper);
    private fuelOrderMapper = inject(FuelOrderMapper);
    
    getFuelStationOrders(fuelStationId: number): Observable<FuelOrder[]> {
        return this.apiService.get(`api/fuel-stations/${fuelStationId}/fuel-orders`)
            .pipe(map((data) => this.apiService.assertArray(data, this.fuelOrderMapper.fromJson.bind(this.fuelOrderMapper))));
    }

    getAssignedManagers(fuelStationId: number): Observable<Manager[]> {
        return this.apiService.get(`api/fuel-stations/${fuelStationId}/managers`)
            .pipe(map(data => this.apiService.assertArray(data, this.managerMapper.fromJson.bind(this.managerMapper))));
    }

    getFuelStations(): Observable<FuelStation[]> {
        return this.apiService.get("api/fuel-stations/")
            .pipe(map(data => this.apiService.assertArray(data, this.fuelStationMapper.fromJson.bind(this.fuelStationMapper))));
    }
    
    getFuelStationById(fuelStationId: number): Observable<FuelStation> {
        return this.apiService.get(`api/fuel-stations/${fuelStationId}`)
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    changeFuelPrice(fuelStationId: number, fuelGrade: FuelGrade, newFuelPrice: number): Observable<FuelStation> {
        // TODO: Change change-fuel-price to fuel-prices/:fuelGrade
        return this.apiService.put(`api/fuel-stations/${fuelStationId}/change-fuel-price`, 
            { fuelGrade: this.fuelGradeToSlug(fuelGrade), newPrice: newFuelPrice })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    // TODO Maybe change unassign-manager to managers/{managerId}/unassign
    unassignManager(fuelStationId: number, managerId: number): Observable<FuelStation> {
        return this.apiService.put(`api/fuel-stations/${fuelStationId}/unassign-manager`, { managerId })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    // TODO Maybe change assign-manager to managers/{managerId}/assign
    assignManager(fuelStationId: number, managerId: number): Observable<FuelStation> {
        return this.apiService.put(`api/fuel-stations/${fuelStationId}/assign-manager`, { managerId })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    deactivateFuelStation(fuelStationId: number): Observable<FuelStation> {
        return this.apiService.put(`api/fuel-stations/${fuelStationId}/deactivate`)
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    createFuelStation(street: string, buildingNumber: string, city: string, postalCode: string, country: string): Observable<FuelStation> {
        return this.apiService.post("api/fuel-stations/", { street, buildingNumber, city, postalCode, country })
            .pipe(map(data => this.fuelStationMapper.fromJson(data)));
    }

    private fuelGradeToSlug(fuelGrade: FuelGrade): string {
        switch(fuelGrade) {
            case FuelGrade.Diesel:
                return "diesel";
            case FuelGrade.RON_92:
                return "ron-92";
            case FuelGrade.RON_95:
                return "ron-95";
            default:
                throw new Error(`Cannot transform FuelGrade: ${fuelGrade} to slug`);
        }
    }
}