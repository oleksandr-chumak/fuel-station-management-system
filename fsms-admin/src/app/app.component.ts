import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { MenubarModule } from 'primeng/menubar';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './modules/ui/components/layout/header/header.component';
import { ToastModule } from 'primeng/toast';
import { User } from 'fsms-web-api';
import { AuthService, NotLoggedInHeaderComponent } from 'fsms-security';
import { AppLanguage, LanguageService } from './modules/common/language.service';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    MenubarModule,
    CommonModule,
    ToastModule,
    HeaderComponent,
    NotLoggedInHeaderComponent
],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  user$!: Observable<User | null>;
  isLoading = new BehaviorSubject<boolean>(true);
  isLoading$ = this.isLoading.asObservable();
  private authService: AuthService = inject(AuthService);
  private languageService: LanguageService = inject(LanguageService);

  ngOnInit(): void {
    this.user$ = this.authService.getUser();
  }

  onLanguageChange(lang: string): void {
    this.languageService.set(lang as AppLanguage);
  }
}