import { catchError, Observable, throwError } from "rxjs";
import { Manager, ManagerRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { inject, Injectable } from "@angular/core";
import { MessageService } from "primeng/api";
import { GetManagerById } from "../manager-commands";
import { TranslateService } from "@ngx-translate/core";

@Injectable({ providedIn: "root" })
export class GetManagerByIdHandler extends CommandHandler<GetManagerById, Manager> {

    private readonly api = inject(ManagerRestClient);

    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute(command: GetManagerById): Observable<Manager> {
        return this.api.getManagerById(command.managerId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.managerErrorDetail', { managerId: command.managerId })
                });
                return throwError(() => e);
            })
        )
    }

};
