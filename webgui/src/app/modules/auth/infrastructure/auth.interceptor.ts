import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { AuthService } from "../domain/auth.service";
import { MessageService } from "primeng/api";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    private authService: AuthService = inject(AuthService);
    private messageService: MessageService = inject(MessageService);


    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        const token = this.authService.getAccessToken();

        let authReq = req;

        if (token) {
            authReq = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`,
                },
            });
        }

        return next.handle(authReq).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401) {
                    this.authService.logout();
                    this.messageService.add({ summary: "Token Expired", severity: "error", detail: "You were logged out of your account." });
                }
                return throwError(() => error);
            })
        );
    }
}