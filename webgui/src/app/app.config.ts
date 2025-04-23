import { ApplicationConfig, inject, provideAppInitializer, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';

import { routes } from './app.routes';
import { HTTP_INTERCEPTORS, HttpClient, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AuthInterceptor } from './modules/auth/infrastructure/auth.interceptor';
import { MessageService } from 'primeng/api';
import { AppConfigService } from './modules/common/infrastructure/app-config.service';


export const appConfig: ApplicationConfig = {
  providers: [
    provideAppInitializer(() => {
      const appConfigService: AppConfigService = inject(AppConfigService);
      return appConfigService.loadConfig(); 
    }), 
    provideHttpClient(withInterceptorsFromDi()),
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      deps:[AppConfigService],
      multi: true,
    },
    MessageService, 
    provideAnimationsAsync(),
    providePrimeNG({
        theme: {
            preset: Aura
        }
    }),
  ]
};

