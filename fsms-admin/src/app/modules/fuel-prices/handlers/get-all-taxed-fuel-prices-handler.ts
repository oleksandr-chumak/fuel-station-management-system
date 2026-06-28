import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { FuelPriceRestClient, TaxedFuelPrice } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { GetAllTaxedFuelPrices } from "../fuel-price-commands";
import { TranslateService } from "@ngx-translate/core";

@Injectable({ providedIn: "root" })
export class GetAllTaxedFuelPricesHandler extends CommandHandler<GetAllTaxedFuelPrices, TaxedFuelPrice[]> {
    private api = inject(FuelPriceRestClient);
    private messageService = inject(MessageService);
    private translate = inject(TranslateService);

    execute(cmd: GetAllTaxedFuelPrices): Observable<TaxedFuelPrice[]> {
        return this.api.getTaxedFuelPrices(cmd.countryCode).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.taxedFuelPricesErrorDetail')
                });
                return throwError(() => e);
            })
        );
    }
}
