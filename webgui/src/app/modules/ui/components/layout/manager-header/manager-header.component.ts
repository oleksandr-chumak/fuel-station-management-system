import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MessageService, MenuItem } from 'primeng/api';
import { Observable } from 'rxjs';
import { AuthService } from '../../../../auth/domain/auth.service';
import User from '../../../../auth/domain/user.model';
import { CommonModule } from '@angular/common';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { MenubarModule } from 'primeng/menubar';

@Component({
  selector: 'app-manager-header',
  standalone: true, 
  imports: [CommonModule, MenubarModule, Menu, AvatarModule, ButtonModule, RouterModule],
  templateUrl: './manager-header.component.html',
})
export class ManagerHeaderComponent {

  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);
  private router: Router = inject(Router);

  user$: Observable<User | null> = this.authService.getUser();

  get menubarItems(): MenuItem[] {
    return [ 
      { label: "Fuel Stations", routerLink: ["/"] },
    ];
  }

  get menuItems(): MenuItem[] {
    return [ { label: "Logout", icon: "pi pi-power-off", command: () => this.handleLogout() } ];
  }

  private handleLogout() {
    this.messageService.add({ severity: "success", summary: "Success", detail: "You were logged out" });
    this.authService.logout();
    this.router.navigate(["/login"]);
  }

}
