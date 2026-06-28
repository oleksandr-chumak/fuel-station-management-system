import { Component, EventEmitter, Output, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DrawerModule } from 'primeng/drawer';
import { SelectButtonModule } from 'primeng/selectbutton';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

interface NotLoggedInMenuItem {
  translationKey: string;
  routerLink: string;
  icon?: string;
}

@Component({
  selector: 'fsms-security-not-logged-in-header',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ButtonModule,
    DrawerModule,
    SelectButtonModule,
    TranslatePipe,
  ],
  templateUrl: './not-logged-in-header.component.html',
})
export class NotLoggedInHeaderComponent {
  private router: Router = inject(Router);
  private translate: TranslateService = inject(TranslateService);

  @Output() languageChange = new EventEmitter<string>();

  mobileMenuVisible: boolean = false;

  languageOptions: { label: string; value: string }[] = [
    { label: 'EN', value: 'en' },
    { label: 'УК', value: 'uk' },
  ];

  get currentLanguage(): string {
    return this.translate.currentLang() ?? this.translate.getFallbackLang() ?? 'en';
  }

  get items(): NotLoggedInMenuItem[] {
    const admin = this.router.url.startsWith("/admin");
    const loginUrl = admin ? "/admin/login" : "/login";
    return [{ translationKey: "notLoggedInHeader.login", routerLink: loginUrl, icon: "pi pi-sign-in" }];
  }

  onLanguageChange(lang: string) {
    this.translate.use(lang);
    this.languageChange.emit(lang);
  }

  toggleMobileMenu() {
    this.mobileMenuVisible = !this.mobileMenuVisible;
  }

  closeMobileMenu() {
    this.mobileMenuVisible = false;
  }
}
