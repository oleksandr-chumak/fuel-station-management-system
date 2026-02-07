import { Component, inject } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { finalize } from 'rxjs';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { LoginFormComponent, AuthService } from 'fsms-security';

@Component({
  selector: 'app-login-page',
  imports: [LoginFormComponent, PanelModule],
  templateUrl: './login.page.html',
})
export class LoginPage {
  private router: Router = inject(Router);
  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);

  loading: boolean = false;

  handleFormSubmission(data: { email: string; password: string }) {
    this.loading = true;
    this.authService
      .loginAdmin(data.email, data.password)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: "Login successful" });
          this.router.navigate(['admin']);
        },
        error: (error) => {
          console.error('Login failed', error);
          this.messageService.add({ severity: "error", summary: "Error", detail: "Invalid credentials"});
        },
      });
  }
}
