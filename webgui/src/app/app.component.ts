import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { ToastModule } from 'primeng/toast';
import { Observable, BehaviorSubject } from 'rxjs';

import User from './modules/auth/models/user.model';
import { AuthService } from './modules/auth/services/auth.service';
import { AdminHeaderComponent } from './modules/ui/components/layout/admin-header/admin-header.component';
import { ManagerHeaderComponent } from './modules/ui/components/layout/manager-header/manager-header.component';
import { NotLoggedInHeaderComponent } from './modules/ui/components/layout/not-logged-in-header/not-logged-in-header.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet, 
    MenubarModule, 
    CommonModule,
    ToastModule, 
    NotLoggedInHeaderComponent, 
    AdminHeaderComponent, 
    ManagerHeaderComponent
  ],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  title = 'webgui';
  user$!: Observable<User | null>;
  isLoading = new BehaviorSubject<boolean>(true);
  isLoading$ = this.isLoading.asObservable();
  
  private authService: AuthService = inject(AuthService); 
  
  ngOnInit(): void {
    this.user$ = this.authService.getUser();
  }
}