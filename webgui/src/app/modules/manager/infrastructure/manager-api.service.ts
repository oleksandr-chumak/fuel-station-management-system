import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { ApiService } from "../../common/infrastructure/api.service";
import Manager from "../domain/manager.model";
import { plainToInstance } from "class-transformer";
import { FuelStation } from "../../fuel-station/domain/fuel-station.model";

@Injectable({ providedIn: "root" })
export class ManagerApiService {

    private apiService = inject(ApiService);

    getManagerById(managerId: number): Observable<Manager> {
        return this.apiService.get("api/managers/" + managerId)
            .pipe(map((data) => plainToInstance(Manager, data)));
    }

    getManagers(): Observable<Manager[]> {
        return this.apiService.get("api/managers/")
            .pipe(map((data) => plainToInstance(Manager, data as Object[])));
    }

    getManagerFuelStations(managerId: number): Observable<FuelStation[]> {
        return this.apiService.get("api/managers/" + managerId + "/fuel-stations")
            .pipe(map((data) => plainToInstance(FuelStation, data as Object[])));
    }

    terminateManager(managerId: number): Observable<Manager> {
        return this.apiService.put("api/managers/" + managerId)
            .pipe(map((data) => plainToInstance(Manager, data)));
    }

    createManager(firstName: string, lastName: string, email: string): Observable<Manager> {
        return this.apiService.post("api/managers/", { firstName, lastName, email })
            .pipe(map((data) => plainToInstance(Manager, data)));
    }
}