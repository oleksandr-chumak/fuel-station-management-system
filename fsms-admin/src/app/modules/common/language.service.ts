import { Injectable, inject, signal } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

export type AppLanguage = 'en' | 'uk';

const STORAGE_KEY = 'fsms.admin.language';

@Injectable({ providedIn: 'root' })
export class LanguageService {
  private translate = inject(TranslateService);

  readonly available: AppLanguage[] = ['en', 'uk'];
  readonly current = signal<AppLanguage>('en');

  init(): void {
    const saved = (localStorage.getItem(STORAGE_KEY) as AppLanguage | null);
    const initial: AppLanguage = saved && this.available.includes(saved) ? saved : 'en';
    this.translate.addLangs(this.available);
    this.translate.setFallbackLang('en');
    this.translate.use(initial);
    this.current.set(initial);
  }

  set(lang: AppLanguage): void {
    if (!this.available.includes(lang)) return;
    this.translate.use(lang);
    this.current.set(lang);
    localStorage.setItem(STORAGE_KEY, lang);
  }
}
