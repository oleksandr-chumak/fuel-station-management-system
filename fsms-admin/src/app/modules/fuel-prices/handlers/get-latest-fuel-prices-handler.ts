import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { FuelPrice, FuelPriceRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { GetLatestFuelPrices } from "../fuel-price-commands";

@Injectable({ providedIn: "root" })
export class GetLatestFuelPricesHandler extends CommandHandler<GetLatestFuelPrices, FuelPrice[]> {
    private api = inject(FuelPriceRestClient);
    private messageService = inject(MessageService);

    execute(_: GetLatestFuelPrices): Observable<FuelPrice[]> {
        return this.api.getFuelPrices(true).pipe(
            catchError((e) => {
                this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching latest fuel prices" });
                return throwError(() => e);
            })
        );
    }
}
