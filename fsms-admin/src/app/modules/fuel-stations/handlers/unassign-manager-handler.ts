import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStationRestClient, FuelStation, Manager } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { FuelStationStore } from '../fuel-station-store';
import { UnassignManager } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class UnassignManagerHandler extends CommandHandler<UnassignManager, Manager> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);

    execute({ fuelStationId, managerId }: UnassignManager): Observable<Manager> {
        return this.api.unassignManager(fuelStationId, managerId).pipe(
            catchError((e) => {
                this.messageService.add({ 
                    severity: "error", 
                    summary: "Error", 
                    detail: "An error occurred while unassign manager"}
                );
                return throwError(() => e);
            }),
            tap(unassignedManager => {
                this.messageService.add({ 
                    severity: "success", 
                    summary: "Unassigned", 
                    detail: "Manager was successfully unassigned" }
                );

                if(this.store.managers !== null) {
                    this.store.managers = this.store.managers
                        .filter((manager) => manager.credentialsId !== unassignedManager.credentialsId); 
                }

                const clonedFuelStation = this.store.fuelStation.clone();
                clonedFuelStation.unassignManger(unassignedManager.credentialsId);
                this.store.fuelStation = clonedFuelStation;
            })
        );
    }
}