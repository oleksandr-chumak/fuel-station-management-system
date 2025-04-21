import { inject, Injectable } from "@angular/core";
import FuelOrder from "./fuel-order.model";
import { FuelOrderApiService } from "../infrastructure/fuel-order-api.service";
import { tap } from "rxjs";

@Injectable({ providedIn: "root" })
export default class FuelOrderService {

    private fuelOrderApiService: FuelOrderApiService = inject(FuelOrderApiService);

    confirm(order: FuelOrder) {
        return this.fuelOrderApiService.confirmFuelOrder(order.id)
            .pipe(tap((o) => o.confirm())); 
    }

    reject(order: FuelOrder) {
        return this.fuelOrderApiService.rejectFuelOrder(order.id)
            .pipe(tap((o) => o.reject())); 
    }
}