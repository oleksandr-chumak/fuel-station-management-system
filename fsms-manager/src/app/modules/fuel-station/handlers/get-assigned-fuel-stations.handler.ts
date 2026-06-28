import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelStation, ManagerRestClient } from "fsms-web-api";
import { MessageService } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";
import { CommandHandler } from "../../common/command-handler";
import { GetAssignedFuelStations } from "../fuel-station-commands";
import { AssignedFuelStationsStore } from "../assigned-fuel-stations-store";

@Injectable({ providedIn: "root" })
export class GetAssignedFuelStationsHandler extends CommandHandler<GetAssignedFuelStations, FuelStation[]> {

    private readonly api = inject(ManagerRestClient);
    private readonly messageService = inject(MessageService);
    private readonly assignedFuelStationsStore = inject(AssignedFuelStationsStore);
    private readonly translate = inject(TranslateService);

    execute({ managerId }: GetAssignedFuelStations): Observable<FuelStation[]> {
        return this.api.getAssignedFuelStations(managerId).pipe(
            tap((fuelStations) => this.assignedFuelStationsStore.fuelStations = fuelStations),
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant("common.error"),
                    detail: this.translate.instant("toasts.fetch.fuelStationsErrorDetail")
                });
                return throwError(() => e);
            })
        );
    }
}
