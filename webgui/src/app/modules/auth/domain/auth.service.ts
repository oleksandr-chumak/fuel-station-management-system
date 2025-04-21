import { inject, Injectable } from "@angular/core";
import { BehaviorSubject, catchError, Observable, of, tap } from "rxjs";
import User from "./user.model";
import AuthApiService from "../infrastructure/auth-api.service";

@Injectable({ providedIn: "root" })
export class AuthService {
    private ACCESS_TOKEN_KEY = "accessToken";
    private accessToken: string | null = this.getAccessTokenFromLocalStorage(); 
    private userSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
    private user$: Observable<User | null> = this.userSubject.asObservable();

    private authApiService: AuthApiService = inject(AuthApiService);

    loadUserData(): Observable<User | null> {
        const token = this.getAccessToken();
        if (token) {
            return this.authApiService.getMe(token as string)
                .pipe(
                    tap(user => {
                        this.userSubject.next(user);
                    }),
                    catchError(err => {
                        console.log("Error happened while fetching user", err);
                        this.logout();
                        return of(null);
                    })
                );
        } else {
            return of(null);
        }
    }

    getUser(): Observable<User | null> {
        return this.user$;
    }

    getAccessToken(): String | null {
        return this.accessToken;
    }

    loginAdmin(email: string, password: string): Observable<string> {
        return this.authApiService.loginAdmin(email, password).pipe(tap((token) => this.handleLogin(token)));
    }

    loginManager(email: string, password: string): Observable<string> {
        return this.authApiService.loginManager(email, password).pipe(tap((token) => this.handleLogin(token)));
    }    

    logout(): void {
        this.accessToken = null;
        this.userSubject.next(null);
        this.removeAccessTokenFromLocalStorage();
    }
    
    private handleLogin(token: string) {
        this.accessToken = token; 
        this.saveAccessTokenToLocalStorage(token);
        this.authApiService.getMe(token)
            .subscribe({
                next: (user) => {
                    this.userSubject.next(user);
                },
                error: (err) => {
                   console.log("Error happened while fetching user", err); 
                }
            });
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
}