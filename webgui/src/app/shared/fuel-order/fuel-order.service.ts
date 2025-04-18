import { Observable } from "rxjs"
import { FuelOrder } from "./fuel-order.model"
import { inject, Injectable } from "@angular/core"
import { ApiService, FuelGrade } from "../api"

@Injectable({ providedIn: "root"})
export class FuelOrderService {
      
    private apiService: ApiService = inject(ApiService);
    
    getFuelOrders(): Observable<FuelOrder[]> {
        return this.apiService.get("api/fuel-orders/");
    } 

    getFuelOrderById(fuelOrderId: number): Observable<FuelOrder> {
        return this.apiService.get("api/fuel-orders/" + fuelOrderId);
    }
    
    confirmFuelOrder(fuelOrderId: number) {
        return this.apiService.put("api/fuel-orders/" + fuelOrderId + "/confirm");
    }

    rejectFuelOrder(fuelOrderId: number) {
        return this.apiService.put("api/fuel-orders/" + fuelOrderId + "/reject");
    }

    createFuelOrder(fuelStationId: number, fuelGrade: FuelGrade, amount: number) {
        return this.apiService.post("api/fuel-orders/", { fuelStationId, fuelGrade, amount});
    }

}