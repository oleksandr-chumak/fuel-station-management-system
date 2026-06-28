import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { FuelPriceRestClient, TaxedFuelPrice } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { GetLatestTaxedFuelPrices } from "../fuel-price-commands";
import { TranslateService } from "@ngx-translate/core";

@Injectable({ providedIn: "root" })
export class GetLatestTaxedFuelPricesHandler extends CommandHandler<GetLatestTaxedFuelPrices, TaxedFuelPrice[]> {
    private api = inject(FuelPriceRestClient);
    private messageService = inject(MessageService);
    private translate = inject(TranslateService);

    execute(cmd: GetLatestTaxedFuelPrices): Observable<TaxedFuelPrice[]> {
        return this.api.getTaxedFuelPrices(cmd.countryCode, true).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.recommendedPricesErrorDetail')
                });
                return throwError(() => e);
            })
        );
    }
}
