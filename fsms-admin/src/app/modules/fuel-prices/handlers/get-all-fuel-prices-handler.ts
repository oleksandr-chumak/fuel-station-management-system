import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { FuelPrice, FuelPriceRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { GetAllFuelPrices } from "../fuel-price-commands";
import { TranslateService } from "@ngx-translate/core";

@Injectable({ providedIn: "root" })
export class GetAllFuelPricesHandler extends CommandHandler<GetAllFuelPrices, FuelPrice[]> {
    private api = inject(FuelPriceRestClient);
    private messageService = inject(MessageService);
    private translate = inject(TranslateService);

    execute(_: GetAllFuelPrices): Observable<FuelPrice[]> {
        return this.api.getFuelPrices().pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.allFuelPricesErrorDetail')
                });
                return throwError(() => e);
            })
        );
    }
}
