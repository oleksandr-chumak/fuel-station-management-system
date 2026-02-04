import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs';
import { AuthService } from '../../modules/auth/services/auth.service';
import { LoginFormComponent } from '../../modules/auth/components/login-form/login-form.component';
import { PanelModule } from 'primeng/panel';

@Component({
  selector: 'app-manager-login-page',
  imports: [LoginFormComponent, PanelModule],  
  templateUrl: './manager-login.component.html'
})
export class ManagerLoginComponent {
  private router: Router = inject(Router);
  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);

  loading: boolean = false;

  handleFormSubmission(data: { email: string; password: string }) {
    this.loading = true;
    this.authService
      .loginManager(data.email, data.password)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: "Login successful" });
          console.log("redirect")
          console.log("user", this.authService.getUserValue())
          this.router.navigate(['/']);
        },
        error: (error) => {
          console.error('Login failed', error);
          this.messageService.add({ severity: "error", summary: "Error", detail: "Invalid credentials"});
        },
      });
  }

}
