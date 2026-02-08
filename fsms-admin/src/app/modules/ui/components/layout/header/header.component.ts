import { Component, inject } from "@angular/core";
import { MenuItem, MessageService } from "primeng/api";
import { AvatarModule } from "primeng/avatar";
import { Observable } from "rxjs";
import { CommonModule } from "@angular/common";
import { ButtonModule } from "primeng/button";
import { Menu } from "primeng/menu";
import { Router, RouterModule } from "@angular/router";
import { PanelModule } from "primeng/panel";
import { DrawerModule } from 'primeng/drawer';
import { User } from "fsms-web-api";
import { AuthService } from "fsms-security";

@Component({
  selector: "app-header",
  standalone: true,
  imports: [
    CommonModule, 
    Menu, 
    AvatarModule, 
    ButtonModule, 
    RouterModule, 
    PanelModule,
    DrawerModule
  ],
  templateUrl: "./header.component.html",
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
      { label: "Managers", routerLink: ["/managers"] },
      { label: "Fuel Orders", routerLink: ["/fuel-orders"] },
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