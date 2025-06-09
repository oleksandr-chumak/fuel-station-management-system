import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { ApiService } from '../../common/api/api.service';
import { UserMapper } from './user.mapper';
import { User } from './models/user.model';

@Injectable({ providedIn: 'root' })
export class AuthApiService {
  private apiService: ApiService = inject(ApiService);
  private userMapper: UserMapper = inject(UserMapper);

  loginAdmin(email: string, password: string): Observable<string> {
    return this.apiService.post<string>('api/auth/login/admin', { email, password }, { responseType: 'text' });
  }

  loginManager(email: string, password: string): Observable<string> {
    return this.apiService.post<string>('api/auth/login/manager', { email, password }, { responseType: 'text' });
  }    

  getMe(): Observable<User> {
    return this.apiService.get('api/auth/me')
      .pipe(map((user) => this.userMapper.fromJson(user)));
  }
}