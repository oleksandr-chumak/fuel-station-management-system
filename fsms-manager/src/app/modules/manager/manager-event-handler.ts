import { inject, Injectable } from "@angular/core";
import {
    FuelStationRestClient,
    ManagerAssignedToFuelStation,
    ManagerStompClient,
    ManagerUnassignedFromFuelStation,
} from "fsms-web-api";
import { catchError, EMPTY, merge, Observable, tap } from "rxjs";
import { LoggerService } from "../common/logger";
import { AssignedFuelStationsStore } from "../fuel-station/assigned-fuel-stations-store";

@Injectable({ providedIn: "root" })
export class ManagerEventHandler {

    private readonly logger = inject(LoggerService);

    private readonly managerStompClient = inject(ManagerStompClient);
    private readonly assignedFuelStationsStore = inject(AssignedFuelStationsStore);
    private readonly fuelStationRestClient = inject(FuelStationRestClient);

    start(managerId: number): Observable<ManagerAssignedToFuelStation | ManagerUnassignedFromFuelStation> {
        this.logger.log('[ManagerEventHandler] Starting event subscription for managerId:', managerId);
        return merge(
            this.managerStompClient.onAssignedToFuelStation(managerId),
            this.managerStompClient.onUnassignedFromFuelStation(managerId)
        ).pipe(
            tap((event) => {
                this.logger.log('[ManagerEventHandler] Event received:', event.constructor.name, event);
                if (event instanceof ManagerAssignedToFuelStation) {
                    this.handleAssigned(event);
                } else if (event instanceof ManagerUnassignedFromFuelStation) {
                    this.handleUnassigned(event);
                }
            })
        );
    }

    private handleAssigned(event: ManagerAssignedToFuelStation): void {
        if (this.assignedFuelStationsStore.fuelStations.some(fs => fs.fuelStationId === event.fuelStationId)) {
            return;
        }

        this.fuelStationRestClient.getFuelStationById(event.fuelStationId)
            .pipe(
                tap((fuelStation) => {
                    if (this.assignedFuelStationsStore.fuelStations.some(fs => fs.fuelStationId === fuelStation.fuelStationId)) {
                        return;
                    }
                    this.assignedFuelStationsStore.fuelStations = [fuelStation, ...this.assignedFuelStationsStore.fuelStations];
                }),
                catchError((e) => {
                    this.logger.error('[ManagerEventHandler] Error fetching fuel station:', e);
                    return EMPTY;
                })
            )
            .subscribe();
    }

    private handleUnassigned(event: ManagerUnassignedFromFuelStation): void {
        this.assignedFuelStationsStore.fuelStations = this.assignedFuelStationsStore.fuelStations.filter(
            (fs) => fs.fuelStationId !== event.fuelStationId
        );
    }
}
