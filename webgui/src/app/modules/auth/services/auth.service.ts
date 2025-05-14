import { inject, Injectable } from "@angular/core";
import { BehaviorSubject, catchError, firstValueFrom, Observable, of, switchMap, tap, throwError } from "rxjs";
import User from "../models/user.model";
import AuthApiService from "./auth-api.service";

@Injectable({ providedIn: "root" })
export class AuthService {
    private ACCESS_TOKEN_KEY = "accessToken";
    private accessToken: string | null = this.getAccessTokenFromLocalStorage();
    private userSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
    private user$: Observable<User | null> = this.userSubject.asObservable();
    private authApiService: AuthApiService = inject(AuthApiService);

    loadUserData(): Promise<User | null> {
        const token = this.getAccessToken();
        if(!token) {
            return Promise.resolve(null);
        }
        return firstValueFrom(this.authApiService.getMe()
            .pipe(
                tap(user => {
                    this.userSubject.next(user);
                }),
                catchError(err => {
                    console.log("Error happened while fetching user", err);
                    this.logout();
                    return of(null);
                })
            ))
    }

    getUser(): Observable<User | null> {
        return this.user$;
    }

    getUserValue(): User | null {
        return this.userSubject.value;
    }

    getAccessToken(): String | null {
        return this.accessToken;
    }

    loginAdmin(email: string, password: string): Observable<User> {
        return this.authApiService.loginAdmin(email, password).pipe(
            tap((token) => this.saveAccessTokenAndSetState(token)),
            switchMap(() => this.authApiService.getMe()),
            tap(user => this.userSubject.next(user)),
            catchError(err => {
                console.log("Error happened while fetching user", err);
                return throwError(() => new Error("Error happened while fetching user" + err)); 
            })
        );
    }

    loginManager(email: string, password: string): Observable<User> {
        return this.authApiService.loginManager(email, password).pipe(
            tap((token) => this.saveAccessTokenAndSetState(token)),
            switchMap(() => this.authApiService.getMe()),
            tap(user => this.userSubject.next(user)),
            catchError(err => {
                console.log("Error happened while fetching user", err);
                return throwError(() => new Error("Error happened while fetching user" + err)); 
            })
        );
    }

    logout(): void {
        this.accessToken = null;
        this.userSubject.next(null);
        this.removeAccessTokenFromLocalStorage();
    }

    private saveAccessTokenAndSetState(token: string): void {
        this.accessToken = token;
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
}