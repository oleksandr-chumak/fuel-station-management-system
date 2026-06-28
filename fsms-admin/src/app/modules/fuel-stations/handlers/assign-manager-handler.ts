import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStationRestClient, Manager } from 'fsms-web-api';
import { FuelStationStore } from '../stores/fuel-station-store';
import { CommandHandler } from '../../common/command-handler';
import { AssignManager } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class AssignManagerHandler extends CommandHandler<AssignManager, Manager> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, managerId }: AssignManager): Observable<Manager> {
        return this.api.assignManager(fuelStationId, managerId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.assignManager.errorDetail')
                });
                return throwError(() => e);
            }),
            tap((manager) => {
                this.messageService.add({
                    severity: "success",
                    summary: this.translate.instant('toasts.assignManager.successSummary'),
                    detail: this.translate.instant('toasts.assignManager.successDetail')
                });

                if (this.store.fuelStation.isManagerAssigned(manager.managerId)) {
                    return;
                }

                const clonedFuelStation = this.store.fuelStation.clone();
                clonedFuelStation.assignManger(manager.managerId);
                this.store.fuelStation = clonedFuelStation;

                if(this.store.managers === null) {
                    return;
                }
                this.store.managers = [...this.store.managers, manager];
            })
        );
    }
}
