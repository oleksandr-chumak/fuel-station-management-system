import { inject, Injectable } from "@angular/core";
import { ApiService } from "../../common/infrastructure/api.service";
import { map, Observable } from "rxjs";
import User from "../domain/user.model";
import { plainToInstance } from "class-transformer";

@Injectable({ providedIn: "root" })
export default class AuthApiService {

    private apiService: ApiService = inject(ApiService);

    loginAdmin(email: string, password: string): Observable<string> {
        return this.apiService.post<string>("api/auth/login/admin", { email, password }, { responseType: "text" });
    }

    loginManager(email: string, password: string): Observable<string> {
        return this.apiService.post<string>("api/auth/login/manager", { email, password }, { responseType: "text" });
    }    

    getMe(): Observable<User> {
        return this.apiService.get<User>("api/auth/me")
            .pipe(map((user) => plainToInstance(User, user)));
    }
    
}