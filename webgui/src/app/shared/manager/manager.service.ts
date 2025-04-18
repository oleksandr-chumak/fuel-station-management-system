import { inject, Injectable } from "@angular/core";
import { ApiService } from "../api";
import { Observable } from "rxjs";
import { Manager } from "./manager.model";

@Injectable({ providedIn: "root" })
export class ManagerService {

    private apiService = inject(ApiService);

    getManagerById(managerId: number): Observable<Manager> {
        return this.apiService.get("api/managers/" + managerId);
    }

    getManagers(): Observable<Manager[]> {
        return this.apiService.get("api/managers/");
    }

    terminateManager(managerId: number): Observable<Manager> {
        return this.apiService.put("api/managers/" + managerId);
    }

    createManager(firstName: string, lastName: string, email: string) {
        return this.apiService.put("api/managers/", { firstName, lastName, email });
    }
}