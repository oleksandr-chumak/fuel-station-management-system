import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { WEB_API_CONFIG } from "./web-api-config.interface";

@Injectable({ providedIn: "root" })
export class RestClient {

    private http = inject(HttpClient);
    private config = inject(WEB_API_CONFIG);

    get<T>(endpoint: string, options?: {
        params?: Record<string, string>,
        responseType?: string 
    }): Observable<T>   {
        return this.http.get<T>(this.getUrl(endpoint), options as any) as Observable<T>;
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

    assertArray<T>(data: unknown, transformer?: (item: unknown) => T): T[] {
        if (!Array.isArray(data)) {
            throw new Error("Expected array response from API");
        }

        return transformer ? data.map(transformer) : data as T[];
    } 

    private get baseUrl() {
        return this.config.restApiUrl();
    }

    private getUrl(endpoint: string) {
        return this.baseUrl + "/" + endpoint;
    }
}