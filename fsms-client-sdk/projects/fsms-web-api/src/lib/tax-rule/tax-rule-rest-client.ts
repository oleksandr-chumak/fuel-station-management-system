import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RestClient } from "../core/rest-client";
import { TaxRule } from "./tax-rule-response";

@Injectable({ providedIn: "root" })
export class TaxRuleRestClient {
    private restClient = inject(RestClient);

    getTaxRulesByCountry(countryCode: string, effective = false): Observable<TaxRule[]> {
        return this.restClient.get<TaxRule[]>(`api/countries/${countryCode}/tax-rules`, { params: { effective } });
    }
}
