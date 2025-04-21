import { Component, inject } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { AvatarModule } from 'primeng/avatar';
import { AuthService } from '../../../../auth/domain/auth.service';
import { Observable } from 'rxjs';
import User from '../../../../auth/domain/user.model';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-header',
  standalone: true,
  imports: [CommonModule, MenubarModule, Menu, AvatarModule, ButtonModule],
  templateUrl: './admin-header.component.html',
})
export class AdminHeaderComponent {

  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);
  private router: Router = inject(Router);

  user$: Observable<User | null> = this.authService.getUser();

  get menubarItems(): MenuItem[] {
    return [];
  }

  get menuItems(): MenuItem[] {
    return [ { label: 'Logout', icon: 'pi pi-power-off', command: () => this.handleLogout() } ];
  }

  private handleLogout() {
    this.messageService.add({ severity: "success", summary: "Success", detail: "You were logged out" });
    this.authService.logout();
    this.router.navigate(["/admin/login"]);
  }
}
