import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';

@Component({
  selector: 'app-not-logged-in-header',
  standalone: true,
  imports: [MenubarModule],
  templateUrl: './not-logged-in-header.component.html',
})
export class NotLoggedInHeaderComponent {

  private router: Router = inject(Router);
  
  get items(): MenuItem[] {
    const admin = this.router.url.startsWith("/admin");
    const loginUrl = admin ? "/admin/login" : "/login";
    return [{label: "Login", route: loginUrl, icon: "pi pi-sign-in" }]
    
  }

}
