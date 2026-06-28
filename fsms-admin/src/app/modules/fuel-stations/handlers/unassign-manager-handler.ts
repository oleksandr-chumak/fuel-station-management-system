import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStationRestClient, Manager } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { FuelStationStore } from '../stores/fuel-station-store';
import { UnassignManager } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class UnassignManagerHandler extends CommandHandler<UnassignManager, Manager> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, managerId }: UnassignManager): Observable<Manager> {
        return this.api.unassignManager(fuelStationId, managerId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.unassignManager.errorDetail')
                });
                return throwError(() => e);
            }),
            tap(unassignedManager => {
                this.messageService.add({
                    severity: "success",
                    summary: this.translate.instant('toasts.unassignManager.successSummary'),
                    detail: this.translate.instant('toasts.unassignManager.successDetail')
                });

                if(this.store.managers !== null) {
                    this.store.managers = this.store.managers
                        .filter((manager) => manager.managerId !== unassignedManager.managerId);
                }

                const clonedFuelStation = this.store.fuelStation.clone();
                clonedFuelStation.unassignManger(unassignedManager.managerId);
                this.store.fuelStation = clonedFuelStation;
            })
        );
    }
}
