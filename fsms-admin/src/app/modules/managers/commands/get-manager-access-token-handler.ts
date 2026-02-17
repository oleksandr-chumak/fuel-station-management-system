import { inject, Injectable } from "@angular/core";
import { CommandHandler } from "../../common/command-handler";
import { GetManagerAccessToken } from "../manager-commands";
import { Observable, catchError, throwError } from "rxjs";
import { AuthRestClient } from "fsms-web-api";
import { MessageService } from "primeng/api";

@Injectable({ providedIn: "root" })
export class GetManagerAccessTokenHandler extends CommandHandler<GetManagerAccessToken, String> {

    private messageService = inject(MessageService);
    private api = inject(AuthRestClient);

    execute(command: GetManagerAccessToken): Observable<String> {
        return this.api.getManagerAccessToken(command.managerId).pipe(
            catchError(error => {
                this.messageService.add({
                    severity: "error",
                    summary: "Error",
                    detail: "Failed to retrieve access token for this manager"
                });
                return throwError(() => error);
            })
        )
    }
}