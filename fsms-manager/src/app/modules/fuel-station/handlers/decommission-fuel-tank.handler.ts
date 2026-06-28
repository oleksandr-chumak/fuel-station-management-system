import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelStation, FuelStationRestClient } from "fsms-web-api";
import { HttpErrorResponse } from "@angular/common/http";
import { MessageService } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { CommandHandler } from "../../common/command-handler";
import { DecommissionFuelTank } from "../fuel-station-commands";
import { FuelStationStore } from "../fuel-station-store";

interface ApiErrorBody {
    code?: string;
    message?: string;
    details?: Record<string, unknown>;
}

@Injectable({ providedIn: "root" })
export class DecommissionFuelTankHandler extends CommandHandler<DecommissionFuelTank, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, fuelTankId }: DecommissionFuelTank): Observable<FuelStation> {
        return this.api.decommissionFuelTank(fuelStationId, fuelTankId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant("common.error"),
                    detail: this.resolveErrorDetail(e)
                });
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({
                    severity: "success",
                    summary: this.translate.instant("toasts.decommissionFuelTank.successSummary"),
                    detail: this.translate.instant("toasts.decommissionFuelTank.successDetail", { fuelTankId })
                });
                this.store.fuelStation = fuelStation;
            })
        );
    }

    private resolveErrorDetail(error: unknown): string {
        if (error instanceof HttpErrorResponse) {
            if (error.status === 403) {
                return this.translate.instant("toasts.decommissionFuelTankForbidden");
            }
            const body = error.error as ApiErrorBody | null;
            if (body?.code === "FUEL_STATION_TANK_HAS_PENDING_FUEL_ORDERS") {
                const details = body.details ?? {};
                const pendingIds = details["pendingFuelOrderIds"] as number[] | undefined;
                return this.translate.instant("toasts.decommissionFuelTank.hasPendingOrdersDetail", {
                    fuelTankId: details["fuelTankId"],
                    pendingOrderIds: (pendingIds ?? []).join(", "),
                    count: pendingIds?.length ?? 0
                });
            }
        }
        return this.translate.instant("toasts.decommissionFuelTank.errorDetail");
    }
}
