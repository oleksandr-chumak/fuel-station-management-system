import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AppConfigService } from "./app-config.service";

@Injectable({ providedIn: "root" })
export class ApiService {
    private http: HttpClient = inject(HttpClient);
    
    constructor(private appConfigService: AppConfigService) {
    }

    private get baseUrl() {
        return this.appConfigService.getConfig().apiUrl;
    }

    get<T>(endpoint: string): Observable<T>   {
        return this.http.get<T>(this.getUrl(endpoint));
    }

    post<T>(endpoint: string, body: Record<string, unknown>, options?: Record<string, string | string[]>): Observable<T>   {
        return this.http.post<T>(this.getUrl(endpoint), body, options);
    }

    put<T>(endpoint: string, body?: Record<string, unknown>): Observable<T>   {
        return this.http.put<T>(this.getUrl(endpoint), body);
    }

    patch<T>(endpoint: string, body?: Record<string, unknown>): Observable<T>   {
        return this.http.patch<T>(this.getUrl(endpoint), body);
    }

    delete<T>(endpoint: string): Observable<T>   {
        return this.http.delete<T>(this.getUrl(endpoint));
    }

    private getUrl(endpoint: string) {
        console.log("baseUrl", this.baseUrl)
        return this.baseUrl + "/" + endpoint;
    }
}