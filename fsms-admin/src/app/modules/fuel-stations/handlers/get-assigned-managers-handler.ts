import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { FuelStationRestClient, Manager } from 'fsms-web-api';
import { CommandHandler } from '../../common/command-handler';
import { GetAssignedManagers } from '../fuel-station-commands';
import { FuelStationStore } from '../stores/fuel-station-store';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class GetAssignedManagersHandler
    extends CommandHandler<GetAssignedManagers, Manager[]> {

    private readonly api = inject(FuelStationRestClient);
    private readonly store = inject(FuelStationStore);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute({ fuelStationId }: GetAssignedManagers): Observable<Manager[]> {
        return this.api.getAssignedManagers(fuelStationId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.managersErrorDetail')
                });
                return throwError(() => e);
            }),
            tap(managers => this.store.managers = managers)
        );
    }
}
