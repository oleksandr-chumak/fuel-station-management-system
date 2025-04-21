import { inject, Injectable } from "@angular/core";
import { ApiService } from "../../common/infrastructure/api.service";
import { Observable, of } from "rxjs";
import User from "../domain/user.model";
import UserRole from "../domain/user-role.enum";

@Injectable({ providedIn: "root" })
export default class AuthApiService {

    private apiService: ApiService = inject(ApiService);

    loginAdmin(email: string, password: string): Observable<string> {
        return this.apiService.post<string>("api/auth/login/admin", { email, password }, { responseType: "text" });
    }

    loginManager(email: string, password: string): Observable<string> {
        return this.apiService.post<string>("api/auth/login/manager", { email, password });
    }    

    getMe(accessToken: string): Observable<User> {
        // TODO implement endpoint on api
        const mockUser = new User("test@test.com", UserRole.Admin);
        return of(mockUser);
    }
    
}