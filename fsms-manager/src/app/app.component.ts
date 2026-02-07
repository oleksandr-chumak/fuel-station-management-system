import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { MenubarModule } from 'primeng/menubar';
import { CommonModule } from '@angular/common';
import { AuthService } from './modules/auth/services/auth.service';
import { ToastModule } from 'primeng/toast';
import { User } from 'fsms-web-api';
import { HeaderComponent } from './modules/ui/components/layout/header/header.component';
import { NotLoggedInHeaderComponent } from './modules/ui/components/layout/not-logged-in-header/not-logged-in-header.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet, 
    MenubarModule, 
    CommonModule,
    ToastModule, 
    NotLoggedInHeaderComponent, 
    HeaderComponent
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