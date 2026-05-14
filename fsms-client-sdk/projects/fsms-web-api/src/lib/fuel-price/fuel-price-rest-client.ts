import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RestClient } from "../core/rest-client";
import { FuelPrice } from "./fuel-price-response";
import { TaxedFuelPrice } from "./taxed-fuel-price-response";

@Injectable({ providedIn: "root" })
export class FuelPriceRestClient {
    private restClient = inject(RestClient);

    getFuelPrices(latest = false): Observable<FuelPrice[]> {
        return this.restClient.get<FuelPrice[]>("api/fuel-prices", { params: { latest } });
    }

    getTaxedFuelPrices(countryCode: string, latest = false): Observable<TaxedFuelPrice[]> {
        return this.restClient.get<TaxedFuelPrice[]>(`api/countries/${countryCode}/fuel-prices`, { params: { latest } });
    }
}
