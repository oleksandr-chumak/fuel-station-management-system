import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { firstValueFrom, tap } from 'rxjs';

export interface AppConfig {
  restApiUrl: string;
  stompApiUrl: string;
}

@Injectable({ providedIn: "root"})
export class AppConfigService {
  private config: AppConfig = {
    restApiUrl: "https://rest-api-url",
    stompApiUrl: "ws://stomp-api-url/ws"
  };

  private http: HttpClient = inject(HttpClient);

  async loadConfig(): Promise<AppConfig> {
    return firstValueFrom(this.http.get<AppConfig>("/assets/app.config.json").pipe(
      tap(data => {
        this.config.restApiUrl = data.restApiUrl;
        this.config.stompApiUrl = data.stompApiUrl;
      })
    ));
  }

  getConfig(): AppConfig {
      return this.config;
  }
}
