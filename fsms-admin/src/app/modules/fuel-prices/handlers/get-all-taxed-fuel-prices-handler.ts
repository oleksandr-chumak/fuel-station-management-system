import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { FuelPriceRestClient, TaxedFuelPriceResponse } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { GetAllTaxedFuelPrices } from "../fuel-price-commands";

@Injectable({ providedIn: "root" })
export class GetAllTaxedFuelPricesHandler extends CommandHandler<GetAllTaxedFuelPrices, TaxedFuelPriceResponse[]> {
    private api = inject(FuelPriceRestClient);
    private messageService = inject(MessageService);

    execute(cmd: GetAllTaxedFuelPrices): Observable<TaxedFuelPriceResponse[]> {
        return this.api.getTaxedFuelPrices(cmd.countryCode).pipe(
            catchError((e) => {
                this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching taxed fuel prices" });
                return throwError(() => e);
            })
        );
    }
}
