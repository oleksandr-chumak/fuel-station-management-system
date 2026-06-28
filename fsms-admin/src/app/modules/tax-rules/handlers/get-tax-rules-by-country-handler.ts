import { inject, Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import { MessageService } from "primeng/api";
import { TaxRule, TaxRuleRestClient } from "fsms-web-api";
import { CommandHandler } from "../../common/command-handler";
import { GetTaxRulesByCountry } from "../tax-rule-commands";
import { TranslateService } from "@ngx-translate/core";

@Injectable({ providedIn: "root" })
export class GetTaxRulesByCountryHandler extends CommandHandler<GetTaxRulesByCountry, TaxRule[]> {
    private api = inject(TaxRuleRestClient);
    private messageService = inject(MessageService);
    private translate = inject(TranslateService);

    execute(cmd: GetTaxRulesByCountry): Observable<TaxRule[]> {
        return this.api.getTaxRulesByCountry(cmd.countryCode, cmd.effective ?? false).pipe(
            catchError((e) => {
                this.messageService.add({
                    severity: "error",
                    summary: this.translate.instant('common.error'),
                    detail: this.translate.instant('toasts.fetch.taxRulesErrorDetail')
                });
                return throwError(() => e);
            })
        );
    }
}
