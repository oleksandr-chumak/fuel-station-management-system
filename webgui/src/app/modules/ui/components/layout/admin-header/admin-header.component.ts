import { Component, inject } from "@angular/core";
import { MenuItem, MessageService } from "primeng/api";
import { MenubarModule } from "primeng/menubar";
import { AvatarModule } from "primeng/avatar";
import { AuthService } from "../../../../auth/services/auth.service";
import { Observable } from "rxjs";
import { CommonModule } from "@angular/common";
import { ButtonModule } from "primeng/button";
import { Menu } from "primeng/menu";
import { Router, RouterModule } from "@angular/router";
import User from "../../../../auth/models/user.model";

@Component({
  selector: "app-admin-header",
  standalone: true,
  imports: [CommonModule, MenubarModule, Menu, AvatarModule, ButtonModule, RouterModule],
  templateUrl: "./admin-header.component.html",
})
export class AdminHeaderComponent {

  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);
  private router: Router = inject(Router);

  user$: Observable<User | null> = this.authService.getUser();

  get menubarItems(): MenuItem[] {
    return [ 
      { label: "Fuel Stations", routerLink: ["/admin"] },
      { label: "Managers", routerLink: ["/admin/managers"] },
      { label: "Fuel Orders", routerLink: ["/admin/fuel-orders"] },
    ];
  }

  get menuItems(): MenuItem[] {
    return [ { label: "Logout", icon: "pi pi-power-off", command: () => this.handleLogout() } ];
  }

  private handleLogout() {
    this.messageService.add({ severity: "success", summary: "Success", detail: "You were logged out" });
    this.authService.logout();
    this.router.navigate(["/admin/login"]);
  }
}
