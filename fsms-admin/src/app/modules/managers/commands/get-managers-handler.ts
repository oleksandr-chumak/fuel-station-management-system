import { inject, Injectable } from "@angular/core";
import { CommandHandler } from "../../common/command-handler";
import { GetManagers } from "../manager-commands";
import { catchError, Observable, throwError } from "rxjs";
import { ManagerRestClient, Manager } from "fsms-web-api";
import { MessageService } from "primeng/api";

@Injectable({ providedIn: "root" })
export class GetManagersHandler extends CommandHandler<GetManagers, Manager[]> {

    private api = inject(ManagerRestClient);
    private messageService = inject(MessageService);

    execute(_: GetManagers): Observable<Manager[]> {
        return this.api.getManagers().pipe(
            catchError((e) => {
                this.messageService.add({ 
                    severity: "error", 
                    summary: "Error", 
                    detail: "An error occurred while fetching managers" }
                );
                return throwError(() => e)
            })
        )
    }
}