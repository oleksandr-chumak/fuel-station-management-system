import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RestClient } from "../core/rest-client";
import { FuelPriceResponse } from "./fuel-price-response";
import { TaxedFuelPriceResponse } from "./taxed-fuel-price-response";

@Injectable({ providedIn: "root" })
export class FuelPriceRestClient {
    private restClient = inject(RestClient);

    getFuelPrices(latest = false): Observable<FuelPriceResponse[]> {
        return this.restClient.get<FuelPriceResponse[]>("api/fuel-prices", { params: { latest } });
    }

    getTaxedFuelPrices(countryCode: string, latest = false): Observable<TaxedFuelPriceResponse[]> {
        return this.restClient.get<TaxedFuelPriceResponse[]>(`api/countries/${countryCode}/fuel-prices`, { params: { latest } });
    }
}
