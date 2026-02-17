import { InjectionToken } from "@angular/core";

export interface WebApiConfig {
    restApiUrl(): string;
    stompApiUrl(): string;
}

export const WEB_API_CONFIG = new InjectionToken<WebApiConfig>("WEB_API_CONFIG");