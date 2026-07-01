import { inject, Injectable } from "@angular/core"
import { CommandHandler } from "../../common/command-handler";
import { ConfirmFuelStationOrder } from "../fuel-station-commands";
import { FuelOrder, FuelOrderRestClient, FuelStationRestClient } from "fsms-web-api";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelStationStore } from "../stores/fuel-station-store";
import { MessageService } from "primeng/api";
import { FuelOrderEventHandler } from "../../fuel-orders/fuel-order-event-handler";
import { TranslateService } from "@ngx-translate/core";


@Injectable({ providedIn: "root" })
export class ConfirmFuelStationOrderHandler
    extends CommandHandler<ConfirmFuelStationOrder, FuelOrder> {

    private readonly store = inject(FuelStationStore);
    private readonly api = inject(FuelOrderRestClient);
    private readonly fuelStationApi = inject(FuelStationRestClient);
    private readonly fuelOrderEventHandler = inject(FuelOrderEventHandler);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute(command: ConfirmFuelStationOrder): Observable<FuelOrder> {
        return this.api.confirmFuelOrder(command.fuelOrderId, command.pricePerLiter)
            .pipe(
                catchError((e) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.error'),
                        detail: this.translate.instant('toasts.confirmFuelOrder.errorDetail')
                    });
                    return throwError(() => e);
                }),
                tap((fuelOrder) => {
                    if(this.store.fuelOrders != null) {
                        this.store.fuelOrders = this.fuelOrderEventHandler
                            .handleFuelOrderConfirmed(fuelOrder.fuelOrderId, this.store.fuelOrders);
                    }

                    this.fuelStationApi.getFuelStationById(fuelOrder.fuelStationId)
                        .subscribe(station => this.store.fuelStation = station);

                    this.messageService.add({
                        severity: 'success',
                        summary: this.translate.instant('toasts.confirmFuelOrder.successSummary'),
                        detail: this.translate.instant('toasts.confirmFuelOrder.successDetail')
                    });
                })
            )
    }

}
