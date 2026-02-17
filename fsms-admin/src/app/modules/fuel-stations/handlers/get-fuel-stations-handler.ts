import { inject, Injectable } from "@angular/core";
import { CommandHandler } from "../../common/command-handler";
import { GetFuelStations } from "../fuel-station-commands";
import { FuelStation, FuelStationRestClient } from "fsms-web-api";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";


@Injectable({providedIn: "root"})
export class GetFuelStationsHandler extends CommandHandler<GetFuelStations, FuelStation[]> {
    private api = inject(FuelStationRestClient)
    private messageService = inject(MessageService);

    execute(_: GetFuelStations): Observable<FuelStation[]> {
        return this.api.getFuelStations().pipe(
            catchError((e) => {
                this.messageService.add({ severity: "error", "summary": "Error", detail: "An error occurred while fetching fuel stations"});
                return throwError(() => e);
            })
        );
    }

}