import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelStation, FuelStationRestClient } from "fsms-web-api";
import { MessageService } from "primeng/api";
import { CommandHandler } from "../../common/command-handler";
import { DispenseFuel } from "../fuel-station-commands";
import { FuelStationStore } from "../fuel-station-store";

@Injectable({ providedIn: "root" })
export class DispenseFuelHandler extends CommandHandler<DispenseFuel, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId, fuelTankId, volume }: DispenseFuel): Observable<FuelStation> {
        return this.api.dispenseFuel(fuelStationId, fuelTankId, volume).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: "Error",
                    detail: "An error occurred while dispensing fuel"
                });
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({
                    severity: "success",
                    summary: "Dispensed",
                    detail: `${volume} L dispensed from tank ${fuelTankId}`
                });
                this.store.fuelStation = fuelStation;
            })
        );
    }
}
