import { catchError, Observable, tap, throwError } from "rxjs";
import { Manager, ManagerRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { CreateManager } from "../manager-commands";
import { inject, Injectable } from "@angular/core";
import { MessageService } from "primeng/api";

@Injectable({ providedIn: "root" })
export class CreateManagerHandler extends CommandHandler<CreateManager, Manager> {

    private readonly api = inject(ManagerRestClient);
    private readonly messageService = inject(MessageService);

    execute(command: CreateManager): Observable<Manager> {
        return this.api.createManager(command.firstName, command.lastName, command.email).pipe(
            catchError((e) => {
                this.messageService.add({ 
                    severity: "error", 
                    summary: "Error", 
                    detail: "An error occurred while creating manager" 
                });
                return throwError(() => e)
            }),
            tap(() => {
                this.messageService.add({ 
                    severity: "success", 
                    summary: "Created", 
                    detail: "A new manager was created" 
                });
            })
        )
    }

}