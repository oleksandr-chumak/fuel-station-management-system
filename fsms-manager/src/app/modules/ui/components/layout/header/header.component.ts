import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MessageService, MenuItem } from 'primeng/api';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { DrawerModule } from 'primeng/drawer';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';
import { User } from 'fsms-web-api';
import { AuthService } from 'fsms-security';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { AppLanguage, LanguageService } from '../../../../common/language.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    Menu,
    AvatarModule,
    ButtonModule,
    RouterModule,
    DrawerModule,
    SelectButtonModule,
    FormsModule,
    TranslatePipe,
  ],
  templateUrl: './header.component.html',
})
export class HeaderComponent {
  private authService: AuthService = inject(AuthService);
  private messageService: MessageService = inject(MessageService);
  private router: Router = inject(Router);
  private translate: TranslateService = inject(TranslateService);
  languageService: LanguageService = inject(LanguageService);

  user$: Observable<User | null> = this.authService.getUser();
  mobileMenuVisible: boolean = false;

  languageOptions: { label: string; value: AppLanguage }[] = [
    { label: 'EN', value: 'en' },
    { label: 'УК', value: 'uk' },
  ];

  get menubarItems(): MenuItem[] {
    return [
      { label: this.translate.instant('header.fuelStations'), routerLink: ['/'] },
    ];
  }

  get menuItems(): MenuItem[] {
    return [ { label: this.translate.instant('common.logout'), icon: 'pi pi-power-off', command: () => this.handleLogout() } ];
  }

  toggleMobileMenu() {
    this.mobileMenuVisible = !this.mobileMenuVisible;
  }

  closeMobileMenu() {
    this.mobileMenuVisible = false;
  }

  onLanguageChange(lang: AppLanguage) {
    this.languageService.set(lang);
  }

  private handleLogout() {
    this.messageService.add({ severity: 'success', summary: this.translate.instant('common.success'), detail: this.translate.instant('header.loggedOut') });
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
