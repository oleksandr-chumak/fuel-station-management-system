import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MessageService, MenuItem } from 'primeng/api';
import { Observable } from 'rxjs';
import { AuthService } from '../../../../auth/services/auth.service';
import { CommonModule } from '@angular/common';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { SidebarModule } from 'primeng/sidebar';
import { User } from 'fsms-web-api';

@Component({
  selector: 'app-header',
  standalone: true, 
  imports: [
    CommonModule, 
    Menu, 
    AvatarModule, 
    ButtonModule, 
    RouterModule,
    SidebarModule
  ],
  templateUrl: './header.component.html',
})
export class HeaderComponent {
  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);
  private router: Router = inject(Router);
  
  user$: Observable<User | null> = this.authService.getUser();
  mobileMenuVisible: boolean = false;
  
  get menubarItems(): MenuItem[] {
    return [ 
      { label: "Fuel Stations", routerLink: ["/"] },
    ];
  }
  
  get menuItems(): MenuItem[] {
    return [ { label: "Logout", icon: "pi pi-power-off", command: () => this.handleLogout() } ];
  }
  
  toggleMobileMenu() {
    this.mobileMenuVisible = !this.mobileMenuVisible;
  }
  
  closeMobileMenu() {
    this.mobileMenuVisible = false;
  }
  
  private handleLogout() {
    this.messageService.add({ severity: "success", summary: "Success", detail: "You were logged out" });
    this.authService.logout();
    this.router.navigate(["/login"]);
  }
}