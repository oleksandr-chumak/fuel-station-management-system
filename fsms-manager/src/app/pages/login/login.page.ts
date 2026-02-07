import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs';
import { PanelModule } from 'primeng/panel';
import { AuthService, LoginFormComponent } from 'fsms-security';

@Component({
  selector: 'app-manager-login-page',
  imports: [LoginFormComponent, PanelModule],  
  templateUrl: './login.page.html'
})
export class LoginPage {
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
