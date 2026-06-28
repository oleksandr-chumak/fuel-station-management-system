import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelOrder, FuelOrderRestClient } from "fsms-web-api";
import { MessageService } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { CommandHandler } from "../../common/command-handler";
import { CreateFuelOrder } from "../fuel-station-commands";
import { FuelStationStore } from "../fuel-station-store";

@Injectable({ providedIn: "root" })
export class CreateFuelOrderHandler extends CommandHandler<CreateFuelOrder, FuelOrder> {

    private readonly api = inject(FuelOrderRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, fuelGrade, amount }: CreateFuelOrder): Observable<FuelOrder> {
        return this.api.createFuelOrder(fuelStationId, fuelGrade, amount).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant("common.error"),
                    detail: this.translate.instant("toasts.createFuelOrder.errorDetail")
                });
                return throwError(() => e);
            }),
            tap(fuelOrder => {
                this.store.fuelOrders = [fuelOrder, ...this.store.fuelOrders];
                this.messageService.add({
                    severity: "success",
                    summary: this.translate.instant("toasts.createFuelOrder.successSummary"),
                    detail: this.translate.instant("toasts.createFuelOrder.successDetail")
                });
            })
        );
    }
}
