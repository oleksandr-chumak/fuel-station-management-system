import { inject, Injectable } from "@angular/core";
import { HttpErrorResponse } from "@angular/common/http";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelOrder, FuelOrderRestClient } from "fsms-web-api";
import { MessageService } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { CommandHandler } from "../../common/command-handler";
import { CreateFuelOrder } from "../fuel-station-commands";
import { FuelStationStore } from "../fuel-station-store";

interface ApiErrorBody {
    code?: string;
    message?: string;
    details?: Record<string, unknown>;
}

@Injectable({ providedIn: "root" })
export class CreateFuelOrderHandler extends CommandHandler<CreateFuelOrder, FuelOrder> {

    private readonly api = inject(FuelOrderRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, fuelGrade, allocations }: CreateFuelOrder): Observable<FuelOrder> {
        return this.api.createFuelOrder(fuelStationId, fuelGrade, allocations).pipe(
            catchError((e: HttpErrorResponse) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant("common.error"),
                    detail: this.resolveErrorDetail(e)
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

    private resolveErrorDetail(error: HttpErrorResponse): string {
        const body = error.error as ApiErrorBody | null;
        if (body?.code === "FUEL_ORDER_ALLOCATION_EXCEEDS_LIMIT") {
            const details = body.details ?? {};
            return this.translate.instant("toasts.createFuelOrder.allocationExceedsLimitDetail", {
                fuelTankId: details["fuelTankId"],
                requestedVolume: details["requestedVolume"],
                allowedVolume: details["allowedVolume"]
            });
        }
        return this.translate.instant("toasts.createFuelOrder.errorDetail");
    }
}
