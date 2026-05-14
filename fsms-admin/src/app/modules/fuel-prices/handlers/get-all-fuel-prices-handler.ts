import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { FuelPrice, FuelPriceRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { GetAllFuelPrices } from "../fuel-price-commands";

@Injectable({ providedIn: "root" })
export class GetAllFuelPricesHandler extends CommandHandler<GetAllFuelPrices, FuelPrice[]> {
    private api = inject(FuelPriceRestClient);
    private messageService = inject(MessageService);

    execute(_: GetAllFuelPrices): Observable<FuelPrice[]> {
        return this.api.getFuelPrices().pipe(
            catchError((e) => {
                this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching fuel price history" });
                return throwError(() => e);
            })
        );
    }
}
