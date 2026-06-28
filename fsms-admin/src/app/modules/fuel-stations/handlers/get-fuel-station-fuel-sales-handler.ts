import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, tap, throwError } from 'rxjs';
import { MessageService } from 'primeng/api';
import { FuelStationRestClient, FuelSale } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetFuelStationFuelSales } from '../fuel-station-commands';
import { FuelStationStore } from '../stores/fuel-station-store';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class GetFuelStationFuelSalesHandler extends CommandHandler<GetFuelStationFuelSales, FuelSale[]> {
    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId }: GetFuelStationFuelSales): Observable<FuelSale[]> {
        return this.api.getFuelStationFuelSales(fuelStationId).pipe(
            catchError(e => {
                this.messageService.add({
                    severity: 'error',
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.fuelSalesErrorDetail')
                });
                return throwError(() => e);
            }),
            tap(sales => this.store.fuelSales = sales)
        );
    }
}
