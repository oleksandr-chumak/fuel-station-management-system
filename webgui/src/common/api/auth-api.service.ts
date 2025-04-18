import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import AuthService from "../security/auth.service";

@Injectable({ providedIn: "root" })
export default class AuthApiService {
    // TODO: Obtain baseUrl from environment variables
    // TODO: Handle the case when the token has expired
    private baseUrl = 'http://localhost:8080';

    constructor(private http: HttpClient, private authService: AuthService) {}

    get<T>(endpoint: string): Observable<T> {
        return this.http.get<T>(this.getUrl(endpoint));
    }

    post<T>(endpoint: string, body: Record<string, unknown>): Observable<T>   {
        return this.http.post<T>(this.getUrl(endpoint), body, { headers: this.getAuthHeaders() });
    }

    put<T>(endpoint: string, body: Record<string, unknown>): Observable<T>   {
        return this.http.put<T>(this.getUrl(endpoint), body, { headers: this.getAuthHeaders() });
    }

    patch<T>(endpoint: string, body: Record<string, unknown>): Observable<T>   {
        return this.http.patch<T>(this.getUrl(endpoint), body, { headers: this.getAuthHeaders() });
    }

    delete<T>(endpoint: string): Observable<T>   {
        return this.http.delete<T>(this.getUrl(endpoint), { headers: this.getAuthHeaders() });
    }

    private getUrl(endpoint: string) {
        return this.baseUrl + "/" + endpoint;
    }

    private getAuthHeaders(): HttpHeaders {
        return new HttpHeaders({
            'Authorization': `Bearer ${this.authService}`,
            'Content-Type': 'application/json'
        });
    }
}