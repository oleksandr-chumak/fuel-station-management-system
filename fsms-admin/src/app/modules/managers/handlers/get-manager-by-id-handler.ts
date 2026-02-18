import { catchError, Observable, tap, throwError } from "rxjs";
import { Manager, ManagerRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { inject, Injectable } from "@angular/core";
import { MessageService } from "primeng/api";
import { GetManagerById } from "../manager-commands";

@Injectable({ providedIn: "root" })
export class GetManagerByIdHandler extends CommandHandler<GetManagerById, Manager> {

    private readonly api = inject(ManagerRestClient);

    private readonly messageService = inject(MessageService);

    execute(command: GetManagerById): Observable<Manager> {
        return this.api.getManagerById(command.managerId).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: "Error",
                    detail: `Failed to retrieve manager with id: ${command.managerId}`
                });
                return throwError(() => e);
            })
        )
    }

};