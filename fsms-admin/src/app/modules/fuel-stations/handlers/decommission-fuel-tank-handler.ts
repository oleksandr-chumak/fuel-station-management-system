import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStation, FuelStationRestClient } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { DecommissionFuelTank } from '../fuel-station-commands';
import { FuelStationStore } from '../stores/fuel-station-store';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class DecommissionFuelTankHandler
    extends CommandHandler<DecommissionFuelTank, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, fuelTankId }: DecommissionFuelTank): Observable<FuelStation> {
        return this.api.decommissionFuelTank(fuelStationId, fuelTankId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: 'error',
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.decommissionFuelTank.errorDetail')
                });
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({
                    severity: 'success',
                    summary: this.translate.instant('toasts.decommissionFuelTank.successSummary'),
                    detail: this.translate.instant('toasts.decommissionFuelTank.successDetail', { fuelTankId })
                });
                this.store.fuelStation = fuelStation;
            })
        );
    }
}
