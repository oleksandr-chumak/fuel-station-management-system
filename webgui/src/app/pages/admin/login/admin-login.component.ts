import { Component, inject } from '@angular/core';
import { LoginFormComponent } from '../../../modules/auth/components/login-form/login-form.component';
import { PanelModule } from 'primeng/panel';
import { AuthService } from '../../../modules/auth/services/auth.service';
import { finalize } from 'rxjs';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-login',
  imports: [LoginFormComponent, PanelModule],
  templateUrl: './admin-login.component.html',
})
export class AdminLoginComponent {
  private router: Router = inject(Router);
  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);

  loading = false;

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
