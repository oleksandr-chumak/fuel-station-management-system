import { map, Observable } from "rxjs"
import { inject, Injectable } from "@angular/core"
import { ApiService } from "../core/web-api.service";
import { FuelOrderMapper } from "./fuel-order.mapper";
import { FuelOrder } from "./fuel-order.model";
import { FuelGrade } from "../core/fuel-grade.enum";

@Injectable({ providedIn: "root"})
export class FuelOrderApiService {
      
    private apiService = inject(ApiService);
    private fuelOrderMapper = inject(FuelOrderMapper);
    
    getFuelOrders(): Observable<FuelOrder[]> {
        return this.apiService.get("api/fuel-orders/")
            .pipe(map(data => this.apiService.assertArray(data, this.fuelOrderMapper.fromJson.bind(this.fuelOrderMapper))));
    } 

    getFuelOrderById(fuelOrderId: number): Observable<FuelOrder> {
        return this.apiService.get(`api/fuel-orders/${fuelOrderId}`)
            .pipe(map(data => this.fuelOrderMapper.fromJson(data)));
    }
    
    confirmFuelOrder(fuelOrderId: number): Observable<FuelOrder> {
        return this.apiService.put(`api/fuel-orders/${fuelOrderId}/confirm`)
            .pipe(map(data => this.fuelOrderMapper.fromJson(data)));
    }

    rejectFuelOrder(fuelOrderId: number): Observable<FuelOrder> {
        return this.apiService.put(`api/fuel-orders/${fuelOrderId}/reject`)
            .pipe(map(data => this.fuelOrderMapper.fromJson(data)));
    }

    createFuelOrder(fuelStationId: number, fuelGrade: FuelGrade, amount: number): Observable<FuelOrder> {
        return this.apiService.post("api/fuel-orders/", { 
            fuelStationId, 
            fuelGrade: this.fuelGradeToSlug(fuelGrade), 
            amount
        })
        .pipe(map(data => this.fuelOrderMapper.fromJson(data)));
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