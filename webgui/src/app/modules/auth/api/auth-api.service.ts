import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { User } from './models/user.model';
import { UserMapper } from './user.mapper';

import { ApiService } from '~common/api/api.service';

@Injectable({ providedIn: 'root' })
export class AuthApiService {
  private apiService: ApiService = inject(ApiService);

  loginAdmin(email: string, password: string): Observable<string> {
    return this.apiService.post<string>('api/auth/login/admin', { email, password }, { responseType: 'text' });
  }

  loginManager(email: string, password: string): Observable<string> {
    return this.apiService.post<string>('api/auth/login/manager', { email, password }, { responseType: 'text' });
  }    

  getMe(): Observable<User> {
    return this.apiService.get('api/auth/me')
      .pipe(map((user) => UserMapper.fromJson(user)));
  }
}