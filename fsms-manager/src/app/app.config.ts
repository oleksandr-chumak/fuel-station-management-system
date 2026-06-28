import { ApplicationConfig, importProvidersFrom, inject, provideAppInitializer, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';

import { routes } from './app.routes';
import { MessageService } from 'primeng/api';
import { AppConfigService } from './modules/common/app-config.service';
import { LanguageService } from './modules/common/language.service';
import { StompClient, WEB_API_CONFIG, WebApiConfig, WebApiModule } from 'fsms-web-api';
import { AuthService, SecurityModule } from 'fsms-security';
import { provideTranslateService } from '@ngx-translate/core';
import { provideTranslateHttpLoader } from '@ngx-translate/http-loader';

export const appConfig: ApplicationConfig = {
  providers: [
    provideAppInitializer(async () => {
      const appConfigService = inject(AppConfigService);
      const stompClient = inject(StompClient);
      const authService = inject(AuthService);
      const languageService = inject(LanguageService);

      await appConfigService.loadConfig();
      languageService.init();
      stompClient.activate();
      return authService.loadUserData();
    }),

    importProvidersFrom(WebApiModule.forRoot()),
    importProvidersFrom(SecurityModule),
    {
      provide: WEB_API_CONFIG,
      useFactory: (appConfigService: AppConfigService): WebApiConfig => {
          const config = appConfigService.getConfig();
          return {
            restApiUrl: () => config.restApiUrl,
            stompApiUrl: () => config.stompApiUrl
          }
      },
      deps: [AppConfigService]
    },

    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),

    provideTranslateService({
      fallbackLang: 'en',
      lang: 'en',
    }),
    provideTranslateHttpLoader({
      prefix: '/assets/i18n/',
      suffix: '.json',
    }),

    MessageService,
    provideAnimationsAsync(),
    providePrimeNG({
        theme: {
            preset: Aura
        }
    }),
  ]
};
