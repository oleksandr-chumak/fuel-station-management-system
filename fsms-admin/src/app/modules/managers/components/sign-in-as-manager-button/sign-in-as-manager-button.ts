import { Component, DestroyRef, inject, input } from "@angular/core";
import { takeUntilDestroyed, toSignal } from "@angular/core/rxjs-interop";
import { ButtonModule } from "primeng/button";
import { AppConfigService } from "../../../common/app-config.service";
import { GetManagerAccessTokenHandler } from "../../handlers/get-manager-access-token-handler";

@Component({
    selector: 'app-sign-in-as-manager-button',
    imports: [ButtonModule],
    templateUrl: './sign-in-as-manager-button.html'
})
export class SignInAsManagerButton {
    managerId = input.required<number>();

    private readonly destroyRef = inject(DestroyRef);
    private readonly appConfigService = inject(AppConfigService);
    private readonly getManagerAccessTokenHandler = inject(GetManagerAccessTokenHandler);

    protected readonly loading = toSignal(this.getManagerAccessTokenHandler.loading$, { initialValue: false });

    protected onClick() {
        const managerUrl = this.appConfigService.getConfig().managerUrl;
        this.getManagerAccessTokenHandler
            .handle({ managerId: this.managerId() })
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe(token => window.open(`${managerUrl}/login?token=${token}`));
    }
}
