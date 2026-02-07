import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { LoginFormComponent } from './components/login-form/login-form.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { AuthService } from './services/auth.service';
import { NotLoggedInHeaderComponent } from './components/not-logged-in-header/not-logged-in-header.component';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    LoginFormComponent,
    NotLoggedInHeaderComponent
  ],
  exports: [
    LoginFormComponent,
    NotLoggedInHeaderComponent
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class SecurityModule { }
