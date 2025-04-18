import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({ providedIn: "root" })
export default class PublicApiService {
    // TODO: Obtain baseUrl from environment variables
    private baseUrl = "http://localhost:8080";

    constructor(private http: HttpClient) {}

    get<T>(endpoint: string): Observable<T>   {
        return this.http.get<T>(this.getUrl(endpoint));
    }

    post<T>(endpoint: string, body: Record<string, unknown>): Observable<T>   {
        return this.http.post<T>(this.getUrl(endpoint), body);
    }

    put<T>(endpoint: string, body: Record<string, unknown>): Observable<T>   {
        return this.http.put<T>(this.getUrl(endpoint), body);
    }

    patch<T>(endpoint: string, body: Record<string, unknown>): Observable<T>   {
        return this.http.patch<T>(this.getUrl(endpoint), body);
    }

    delete<T>(endpoint: string): Observable<T>   {
        return this.http.delete<T>(this.getUrl(endpoint));
    }

    private getUrl(endpoint: string) {
        return this.baseUrl + "/" + endpoint;
    }
}