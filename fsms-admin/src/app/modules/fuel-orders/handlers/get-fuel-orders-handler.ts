import { catchError, Observable, throwError } from "rxjs";
import { FuelOrder, FuelOrderRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { inject, Injectable } from "@angular/core";
import { MessageService } from "primeng/api";
import { GetFuelOrders } from "../fuel-order-commands";
import { TranslateService } from "@ngx-translate/core";

@Injectable({ providedIn: "root" })
export class GetFuelOrdersHandler extends CommandHandler<GetFuelOrders, FuelOrder[]> {

    private readonly api = inject(FuelOrderRestClient);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute(_: GetFuelOrders): Observable<FuelOrder[]> {
        return this.api.getFuelOrders().pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.fuelOrdersErrorDetail')
                })
                return throwError(() => e);
            }),
        );
    }

}
