import { FuelStation, FuelStationRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { inject, Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { CreateFuelStation } from "../fuel-station-commands";


@Injectable({ providedIn: "root" })
export class CreateFuelStationHandler extends CommandHandler<CreateFuelStation, FuelStation> {

    private readonly api = inject(FuelStationRestClient);

    private readonly messageService = inject(MessageService);

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
                    summary: "Error",
                    detail: "An error occurred while creating fuel station"
                });
                return throwError(() => e);
            }),
            tap(() => {
                this.messageService.add({
                    severity: "success",
                    summary: "Created",
                    detail: "A new fuel station was created"
                });
            })
        )
    }

}