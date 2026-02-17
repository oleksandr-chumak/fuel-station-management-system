import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { firstValueFrom, tap } from 'rxjs';

export interface AppConfig {
  restApiUrl: string;
  stompApiUrl: string;
  managerUrl: string;
}

@Injectable({ providedIn: "root"})
export class AppConfigService {
  private config: AppConfig = {
    restApiUrl: "https://rest-api-url",
    stompApiUrl: "ws://stomp-api-url/ws",
    managerUrl: "https://manager-url"
  };

  private http: HttpClient = inject(HttpClient);

  async loadConfig(): Promise<AppConfig> {
    return firstValueFrom(this.http.get<AppConfig>("/assets/app.config.json").pipe(
      tap(data => {
        this.config.restApiUrl = data.restApiUrl;
        this.config.stompApiUrl = data.stompApiUrl;
        this.config.managerUrl = data.managerUrl;
      })
    ));
  }
  
  getConfig(): AppConfig {
      return this.config ;
  }
}