import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStation, FuelStationRestClient } from 'fsms-web-api';
import { FuelStationStore } from '../stores/fuel-station-store';
import { CommandHandler } from '../../common/command-handler';
import { UpdateFuelPrices } from '../fuel-station-commands';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class UpdateFuelPricesHandler
    extends CommandHandler<UpdateFuelPrices, FuelStation> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId, prices }: UpdateFuelPrices): Observable<FuelStation> {
        return this.api.updateFuelPrices(fuelStationId, prices).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.updateFuelPrices.errorDetail')
                });
                return throwError(() => e);
            }),
            tap((fuelStation) => {
                this.messageService.add({
                    severity: "success",
                    summary: this.translate.instant('toasts.updateFuelPrices.successSummary'),
                    detail: this.translate.instant('toasts.updateFuelPrices.successDetail')
                });
                this.store.fuelStation = fuelStation.clone();
            })
        );
    }
}
