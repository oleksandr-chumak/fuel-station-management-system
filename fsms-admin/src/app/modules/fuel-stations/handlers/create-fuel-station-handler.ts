import { FuelStation, FuelStationRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { CreateFuelStation } from "../fuel-station-commands";
import { TranslateService } from "@ngx-translate/core";


@Injectable({ providedIn: "root" })
export class CreateFuelStationHandler extends CommandHandler<CreateFuelStation, FuelStation> {

    private readonly api = inject(FuelStationRestClient);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute(command: CreateFuelStation): Observable<FuelStation> {
        return this.api.createFuelStation(
            command.street,
            command.buildingNumber,
            command.city,
            command.postalCode,
            command.country
        ).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.createFuelStation.errorDetail')
                });
                return throwError(() => e);
            }),
            tap(() => {
                this.messageService.add({
                    severity: "success",
                    summary: this.translate.instant('toasts.createFuelStation.successSummary'),
                    detail: this.translate.instant('toasts.createFuelStation.successDetail')
                });
            })
        )
    }

}
