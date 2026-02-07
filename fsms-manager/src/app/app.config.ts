import { ApplicationConfig, importProvidersFrom, inject, provideAppInitializer, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';

import { routes } from './app.routes';
import { MessageService } from 'primeng/api';
import { AppConfigService } from './modules/common/app-config.service';
import { WEB_API_CONFIG, WebApiConfig, WebApiModule } from 'fsms-web-api';
import { AuthService, SecurityModule } from 'fsms-security';

export const appConfig: ApplicationConfig = {
  providers: [
    provideAppInitializer(async () => {
      const appConfigService: AppConfigService = inject(AppConfigService);
      const authService: AuthService = inject(AuthService);
      await appConfigService.loadConfig(); 
      return authService.loadUserData();
    }), 

    importProvidersFrom(WebApiModule.forRoot()),
    {
      provide: WEB_API_CONFIG,
      useFactory: (appConfigService: AppConfigService): WebApiConfig => ({
        getApiUrl: () => {
          const config = appConfigService.getConfig();
          if (!config) {
            throw new Error('AppConfig not loaded yet');
          }
          return config.apiUrl;
        }
      }),
      deps: [AppConfigService]
    },

    SecurityModule,

    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes),

    MessageService, 
    provideAnimationsAsync(),
    providePrimeNG({
        theme: {
            preset: Aura
        }
    }),
  ]
};

