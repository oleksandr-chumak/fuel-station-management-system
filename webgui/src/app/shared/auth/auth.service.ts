import { Injectable } from "@angular/core";
import PublicApiService from "../api/public-api.service";
import { Observable, tap } from "rxjs";
import { User, UserRole } from "./user.model";

@Injectable({ providedIn: "root" })
export class AuthService {
    private ACCESS_TOKEN_KEY = "accessToken";
    private accessToken: string | null = this.getAccessTokenFromLocalStorage(); 
    // TODO: User must be observable? 
    private user: User | null = null;

    constructor(private publicApiService: PublicApiService) {}

    getUser(): User | null {
        return this.user;
    }

    getAccessToken(): String | null {
        return this.accessToken
    }

    isAuthorized(): boolean {
        return !!this.user;
    }

    isAdmin(): boolean {
        return this.user?.role == UserRole.Admin;
    }

    isManager(): boolean {
        return this.user?.role == UserRole.Manager;
    }

    loginAdmin(email: string, password: string): Observable<string> {
        return this.publicApiService.post<string>("api/auth/login/admin", { email, password })
            .pipe(
                tap((token) => {
                    this.accessToken = token; 
                    this.user = this.getUserByAccessToken(token);
                })
            )
    }

    loginManager(email: string, password: string): void {
        this.publicApiService.post<string>("api/auth/login/manager", { email, password })
            .subscribe({
                next: (token) => {
                    this.accessToken = token; 
                    this.user = this.getUserByAccessToken(token);
                    this.saveAccessTokenToLocalStorage(token);
                },
                error: (err) => {
                    // TODO: Handle this error. Idk how to do it in angular
                    console.error('Login failed', err);
                }
            })  
    }    

    logout(): void {
        this.accessToken = null;
        this.user = null;
        this.removeAccessTokenFromLocalStorage();
    }

    private saveAccessTokenToLocalStorage(accessToken: string): void {
        localStorage.setItem(this.ACCESS_TOKEN_KEY, accessToken);
    }

    private getAccessTokenFromLocalStorage(): string | null {
        return localStorage.getItem(this.ACCESS_TOKEN_KEY);
    }

    private removeAccessTokenFromLocalStorage(): void {
        localStorage.removeItem(this.ACCESS_TOKEN_KEY);
    }

    private getUserByAccessToken(accessToken: string): User {
        // TODO: Replace it with api call 
        // TODO: Implement api endpoint to fetch user by access token
        return {email: "test", role: UserRole.Admin};
    }
}