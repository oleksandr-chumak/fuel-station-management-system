import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { catchError, EMPTY, finalize, tap } from 'rxjs';
import { PanelModule } from 'primeng/panel';
import { AuthService, LoginFormComponent } from 'fsms-security';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-manager-login-page',
  imports: [LoginFormComponent, PanelModule, TranslatePipe],
  templateUrl: './login.page.html'
})
export class LoginPage implements OnInit {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService);
  private messageService = inject(MessageService);
  private translate = inject(TranslateService);

  loading: boolean = false;
  token: string | null = null;

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');

    if(this.token) {
      this.authService.setAccessToken(this.token);
      this.authService.loadUserData().then(() => {
        this.router.navigate(["/"]);
      });
    }
  }

  handleFormSubmission(data: { email: string; password: string }) {
    this.loading = true;
    this.authService
      .loginManager(data.email, data.password)
      .pipe(
        catchError(() => {
          this.messageService.add({
            severity: "error",
            summary: this.translate.instant('common.error'),
            detail: this.translate.instant('login.invalidCredentials')
          });
          return EMPTY;
        }),
        tap(() => {
          this.messageService.add({
            severity: 'success',
            summary: this.translate.instant('common.success'),
            detail: this.translate.instant('login.successDetail')
          });
          this.router.navigate(['/']);
        }),
        finalize(() => this.loading = false)
      )
      .subscribe();
  }

}
