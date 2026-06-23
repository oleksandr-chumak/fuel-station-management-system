import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { FuelStation, FuelStationRestClient } from "fsms-web-api";
import { HttpErrorResponse } from "@angular/common/http";
import { MessageService } from "primeng/api";
import { CommandHandler } from "../../common/command-handler";
import { DecommissionFuelTank } from "../fuel-station-commands";
import { FuelStationStore } from "../fuel-station-store";

@Injectable({ providedIn: "root" })
export class DecommissionFuelTankHandler extends CommandHandler<DecommissionFuelTank, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);

    execute({ fuelStationId, fuelTankId }: DecommissionFuelTank): Observable<FuelStation> {
        return this.api.decommissionFuelTank(fuelStationId, fuelTankId).pipe(
            catchError((e) => {
                const detail = e instanceof HttpErrorResponse && e.status === 403
                    ? "Only administrators can decommission tanks"
                    : "An error occurred while decommissioning fuel tank";
                this.messageService.add({
                    severity: "error",
                    summary: "Error",
                    detail
                });
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({
                    severity: "success",
                    summary: "Decommissioned",
                    detail: `Fuel tank ${fuelTankId} was decommissioned`
                });
                this.store.fuelStation = fuelStation;
            })
        );
    }
}
