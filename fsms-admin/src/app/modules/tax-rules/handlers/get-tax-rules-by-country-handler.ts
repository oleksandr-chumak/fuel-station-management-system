import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { TaxRule, TaxRuleRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { GetTaxRulesByCountry } from "../tax-rule-commands";

@Injectable({ providedIn: "root" })
export class GetTaxRulesByCountryHandler extends CommandHandler<GetTaxRulesByCountry, TaxRule[]> {
    private api = inject(TaxRuleRestClient);
    private messageService = inject(MessageService);

    execute(cmd: GetTaxRulesByCountry): Observable<TaxRule[]> {
        return this.api.getTaxRulesByCountry(cmd.countryCode, cmd.effective ?? false).pipe(
            catchError((e) => {
                this.messageService.add({ severity: "error", summary: "Error", detail: "An error occurred while fetching tax rules" });
                return throwError(() => e);
            })
        );
    }
}
