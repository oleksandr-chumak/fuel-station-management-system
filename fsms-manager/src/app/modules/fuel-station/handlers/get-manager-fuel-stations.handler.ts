import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { FuelStation, ManagerRestClient } from "fsms-web-api";
import { MessageService } from "primeng/api";
import { CommandHandler } from "../../common/command-handler";
import { GetManagerFuelStations } from "../fuel-station-commands";

@Injectable({ providedIn: "root" })
export class GetManagerFuelStationsHandler extends CommandHandler<GetManagerFuelStations, FuelStation[]> {

    private readonly api = inject(ManagerRestClient);
    private readonly messageService = inject(MessageService);

    execute({ managerId }: GetManagerFuelStations): Observable<FuelStation[]> {
        return this.api.getManagerFuelStations(managerId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: "Error",
                    detail: "An error occurred while fetching fuel stations"
                });
                return throwError(() => e);
            })
        );
    }
}
