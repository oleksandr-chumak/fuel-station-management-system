import { map, Observable } from "rxjs"
import { inject, Injectable } from "@angular/core"
import { ApiService } from "../../common/infrastructure/api.service";
import FuelOrder from "../domain/fuel-order.model";
import { plainToInstance } from "class-transformer";
import FuelGrade from "../../common/domain/fuel-grade.enum";

@Injectable({ providedIn: "root"})
export class FuelOrderApiService {
      
    private apiService: ApiService = inject(ApiService);
    
    getFuelOrders(): Observable<FuelOrder[]> {
        return this.apiService.get("api/fuel-orders/")
            .pipe(map((data) => plainToInstance(FuelOrder, data as Object[])));
    } 

    getFuelOrderById(fuelOrderId: number): Observable<FuelOrder> {
        return this.apiService.get("api/fuel-orders/" + fuelOrderId)
            .pipe(map((data) => plainToInstance(FuelOrder, data)));
    }
    
    confirmFuelOrder(fuelOrderId: number): Observable<FuelOrder> {
        return this.apiService.put("api/fuel-orders/" + fuelOrderId + "/confirm")
            .pipe(map((data) => plainToInstance(FuelOrder, data)));
    }

    rejectFuelOrder(fuelOrderId: number): Observable<FuelOrder> {
        return this.apiService.put("api/fuel-orders/" + fuelOrderId + "/reject")
            .pipe(map((data) => plainToInstance(FuelOrder, data)));
    }

    createFuelOrder(fuelStationId: number, fuelGrade: FuelGrade, amount: number): Observable<FuelOrder> {
        return this.apiService.post("api/fuel-orders/", { fuelStationId, fuelGrade, amount})
            .pipe(map((data) => plainToInstance(FuelOrder, data)));
    }

}