import { Component, inject } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { finalize } from 'rxjs';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { LoginFormComponent, AuthService } from 'fsms-security';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-login-page',
  imports: [LoginFormComponent, PanelModule, TranslatePipe],
  templateUrl: './login.page.html',
})
export class LoginPage {
  private router: Router = inject(Router);
  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);
  private translate: TranslateService = inject(TranslateService);

  loading: boolean = false;

  handleFormSubmission(data: { email: string; password: string }) {
    this.loading = true;
    this.authService
      .loginAdmin(data.email, data.password)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: this.translate.instant('common.success'), detail: this.translate.instant('login.successDetail') });
          this.router.navigate(['/']);
        },
        error: (error) => {
          console.error('Login failed', error);
          this.messageService.add({ severity: "error", summary: this.translate.instant('common.error'), detail: this.translate.instant('login.invalidCredentials')});
        },
      });
  }
}
