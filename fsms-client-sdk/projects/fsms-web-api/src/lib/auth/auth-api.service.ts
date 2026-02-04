import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { ApiService } from "../core/web-api.service";
import { UserMapper } from "./user.mapper";
import { User } from "./user.model";

@Injectable({ providedIn: "root" })
export class AuthApiService {

    private apiService = inject(ApiService);
    private userMapper = inject(UserMapper);

    loginAdmin(email: string, password: string): Observable<string> {
        return this.apiService.post<string>("api/auth/login/admin", { email, password }, { responseType: "text" });
    }

    loginManager(email: string, password: string): Observable<string> {
        return this.apiService.post<string>("api/auth/login/manager", { email, password }, { responseType: "text" });
    }    

    getMe(): Observable<User> {
        return this.apiService.get("api/auth/me")
            .pipe(map((user) => this.userMapper.fromJson(user)));
    }
    
}