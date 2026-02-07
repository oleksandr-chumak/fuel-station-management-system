import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { firstValueFrom, tap } from 'rxjs';

export interface AppConfig {
  apiUrl: string;
}

@Injectable({ providedIn: "root"})
export class AppConfigService {
  private config!: AppConfig;

  private http: HttpClient = inject(HttpClient);

  async loadConfig(): Promise<AppConfig> {
    return firstValueFrom(this.http.get<AppConfig>("/assets/app.config.json").pipe(tap(data => {this.config  = data;})));
  }
  
  getConfig(): AppConfig {
      return this.config;
  }
}