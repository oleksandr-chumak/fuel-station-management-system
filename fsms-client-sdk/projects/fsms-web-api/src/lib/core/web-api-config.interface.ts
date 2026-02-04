import { InjectionToken } from "@angular/core";

export interface WebApiConfig {
    getApiUrl(): string;
}

export const WEB_API_CONFIG = new InjectionToken<WebApiConfig>("WEB_API_CONFIG");