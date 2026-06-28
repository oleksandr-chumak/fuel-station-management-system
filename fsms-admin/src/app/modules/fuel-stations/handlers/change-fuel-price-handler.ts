import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStation, FuelStationRestClient } from 'fsms-web-api';
import { FuelStationStore } from '../stores/fuel-station-store';
import { CommandHandler } from '../../common/command-handler';
import { ChangeFuelPrice } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class ChangeFuelPriceHandler
    extends CommandHandler<ChangeFuelPrice, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, fuelGrade, newPrice }: ChangeFuelPrice): Observable<FuelStation> {
        return this.api.changeFuelPrice(fuelStationId, fuelGrade, newPrice).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.changeFuelPrice.errorDetail')
                });
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({
                    severity: "success",
                    summary: this.translate.instant('toasts.changeFuelPrice.successSummary'),
                    detail: this.translate.instant('toasts.changeFuelPrice.successDetail')
                });
                this.store.fuelStation = fuelStation.clone();
            })
        );
    }
}
