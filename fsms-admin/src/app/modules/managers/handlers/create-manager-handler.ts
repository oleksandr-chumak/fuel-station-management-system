import { catchError, Observable, tap, throwError } from "rxjs";
import { Manager, ManagerRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { CreateManager } from "../manager-commands";
import { inject, Injectable } from "@angular/core";
import { MessageService } from "primeng/api";
import { TranslateService } from "@ngx-translate/core";

@Injectable({ providedIn: "root" })
export class CreateManagerHandler extends CommandHandler<CreateManager, Manager> {

    private readonly api = inject(ManagerRestClient);
    private readonly messageService = inject(MessageService);
    private readonly translate = inject(TranslateService);

    execute(command: CreateManager): Observable<Manager> {
        return this.api.createManager(command.firstName, command.lastName, command.email).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.createManager.errorDetail')
                });
                return throwError(() => e)
            }),
            tap(() => {
                this.messageService.add({
                    severity: "success",
                    summary: this.translate.instant('toasts.createManager.successSummary'),
                    detail: this.translate.instant('toasts.createManager.successDetail')
                });
            })
        )
    }

}
