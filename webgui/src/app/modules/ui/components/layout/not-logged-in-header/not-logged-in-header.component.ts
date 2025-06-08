import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { SidebarModule } from 'primeng/sidebar';

@Component({
  selector: 'app-not-logged-in-header',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ButtonModule,
    SidebarModule
  ],
  templateUrl: './not-logged-in-header.component.html',
})
export class NotLoggedInHeaderComponent {
  private router: Router = inject(Router);
  mobileMenuVisible = false;
  
  get items(): MenuItem[] {
    const admin = this.router.url.startsWith('/admin');
    const loginUrl = admin ? '/admin/login' : '/login';
    return [{label: 'Login', routerLink: loginUrl, icon: 'pi pi-sign-in' }];
  }
  
  toggleMobileMenu() {
    this.mobileMenuVisible = !this.mobileMenuVisible;
  }
  
  closeMobileMenu() {
    this.mobileMenuVisible = false;
  }
}