import { inject, Injectable } from "@angular/core";
import { Observable, tap } from "rxjs";
import { User, UserRole } from "./user.model";
import { ApiService } from "../api";

@Injectable({ providedIn: "root" })
export class AuthService {
    private ACCESS_TOKEN_KEY = "accessToken";
    private accessToken: string | null = this.getAccessTokenFromLocalStorage(); 
    // TODO: User must be observable? 
    private user: User | null = null;

    private apiService: ApiService = inject(ApiService);

    getUser(): User | null {
        return this.user;
    }

    getAccessToken(): String | null {
        return this.accessToken;
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
        return this.apiService.post<string>("api/auth/login/admin", { email, password }).pipe(tap(this.handleLogin));
    }

    loginManager(email: string, password: string): Observable<string> {
        return this.apiService.post<string>("api/auth/login/admin", { email, password }).pipe(tap(this.handleLogin));
    }    

    logout(): void {
        this.accessToken = null;
        this.user = null;
        this.removeAccessTokenFromLocalStorage();
    }
    
    private handleLogin(token: string) {
        this.accessToken = token; 
        this.user = this.getUserByAccessToken(token);
        this.saveAccessTokenToLocalStorage(token);
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