import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelStation, FuelStationRestClient } from "fsms-web-api";
import { HttpErrorResponse } from "@angular/common/http";
import { MessageService } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { CommandHandler } from "../../common/command-handler";
import { GetFuelStationById } from "../fuel-station-commands";
import { FuelStationStore } from "../fuel-station-store";

@Injectable({ providedIn: "root" })
export class GetFuelStationByIdHandler extends CommandHandler<GetFuelStationById, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId }: GetFuelStationById): Observable<FuelStation> {
        return this.api.getFuelStationById(fuelStationId).pipe(
            catchError((e) => {
                if (e instanceof HttpErrorResponse && e.status == 404) {
                    this.messageService.add({
                        severity: "error",
                        summary: this.translate.instant("toasts.fetch.fuelStationNotFoundSummary"),
                        detail: this.translate.instant("toasts.fetch.fuelStationNotFoundDetail", { fuelStationId })
                    });
                }

                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant("common.error"),
                    detail: this.translate.instant("toasts.fetch.fuelStationErrorDetail", { fuelStationId })
                });

                return throwError(() => e);
            }),
            tap(fuelStation => this.store.fuelStation = fuelStation)
        );
    }
}
