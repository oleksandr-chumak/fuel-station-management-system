import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MenuItem, MessageService } from 'primeng/api';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { PanelModule } from 'primeng/panel';
import { SidebarModule } from 'primeng/sidebar';
import { Observable } from 'rxjs';

import { User } from '../../../../auth/api/models/user.model';
import { AuthService } from '../../../../auth/application/auth.service';

@Component({
  selector: 'app-admin-header',
  standalone: true,
  imports: [
    CommonModule, 
    Menu, 
    AvatarModule, 
    ButtonModule, 
    RouterModule, 
    PanelModule,
    SidebarModule
  ],
  templateUrl: './admin-header.component.html',
})
export class AdminHeaderComponent {
  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);
  private router: Router = inject(Router);
  
  user$: Observable<User | null> = this.authService.getUser();
  mobileMenuVisible = false;
  
  get menubarItems(): MenuItem[] {
    return [ 
      { label: 'Fuel Stations', routerLink: ['/admin'] },
      { label: 'Managers', routerLink: ['/admin/managers'] },
      { label: 'Fuel Orders', routerLink: ['/admin/fuel-orders'] },
    ];
  }
  
  get menuItems(): MenuItem[] {
    return [ { label: 'Logout', icon: 'pi pi-power-off', command: () => this.handleLogout() } ];
  }
  
  toggleMobileMenu() {
    this.mobileMenuVisible = !this.mobileMenuVisible;
  }
  
  closeMobileMenu() {
    this.mobileMenuVisible = false;
  }
  
  private handleLogout() {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'You were logged out' });
    this.authService.logout();
    this.router.navigate(['/admin/login']);
  }
}