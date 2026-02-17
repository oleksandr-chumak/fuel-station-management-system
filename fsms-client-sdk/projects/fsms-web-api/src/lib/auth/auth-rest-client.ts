import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { RestClient } from "../core/rest-client";
import { UserMapper } from "./user.mapper";
import { User } from "./user.model";

@Injectable({ providedIn: "root" })
export class AuthRestClient {

    private restClient = inject(RestClient);
    private userMapper = inject(UserMapper);

    loginAdmin(email: string, password: string): Observable<string> {
        return this.restClient.post<string>("api/auth/admins/login", { email, password }, { responseType: "text" });
    }

    loginManager(email: string, password: string): Observable<string> {
        return this.restClient.post<string>("api/auth/managers/login", { email, password }, { responseType: "text" });
    }

    getManagerAccessToken(managerId: number): Observable<String> {
        return this.restClient.get<string>(`api/auth/managers/${managerId}/token`, { responseType: "text" });
    }

    getMe(): Observable<User> {
        return this.restClient.get("api/auth/me")
            .pipe(map((user) => this.userMapper.fromJson(user)));
    }
    
}