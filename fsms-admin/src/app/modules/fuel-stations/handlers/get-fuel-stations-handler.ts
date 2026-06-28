import { inject, Injectable } from "@angular/core";
import { CommandHandler } from "../../common/command-handler";
import { GetFuelStations } from "../fuel-station-commands";
import { FuelStation, FuelStationRestClient } from "fsms-web-api";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";


@Injectable({providedIn: "root"})
export class GetFuelStationsHandler extends CommandHandler<GetFuelStations, FuelStation[]> {
    private api = inject(FuelStationRestClient)
    private messageService = inject(MessageService);
    private translate = inject(TranslateService);

    execute(_: GetFuelStations): Observable<FuelStation[]> {
        return this.api.getFuelStations().pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.fuelStationsErrorDetail')
                });
                return throwError(() => e);
            })
        );
    }

}
