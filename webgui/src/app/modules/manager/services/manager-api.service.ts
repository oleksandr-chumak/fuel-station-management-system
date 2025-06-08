import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { ApiService } from "../../common/api.service";
import Manager from "../models/manager.model";
import { FuelStation } from "../../fuel-station/models/fuel-station.model";
import { ManagerMapper } from "../mappers/manager.mapper";
import { FuelStationMapper } from "../../fuel-station/mappers/fuel-station.mapper";

@Injectable({ providedIn: "root" })
export class ManagerApiService {

  private apiService = inject(ApiService);

  getManagerById(managerId: number): Observable<Manager> {
    return this.apiService.get("api/managers/" + managerId)
      .pipe(map((ManagerMapper.fromJson)));
  }

  getManagers(): Observable<Manager[]> {
    return this.apiService.get("api/managers/")
      .pipe(map(data => this.apiService.assertArray(data, ManagerMapper.fromJson)));
  }

  getManagerFuelStations(managerId: number): Observable<FuelStation[]> {
    return this.apiService.get("api/managers/" + managerId + "/fuel-stations")
      .pipe(map(data => this.apiService.assertArray(data, FuelStationMapper.fromJson)));
  }

  terminateManager(managerId: number): Observable<Manager> {
    return this.apiService.put("api/managers/" + managerId)
      .pipe(map((data) => ManagerMapper.fromJson(data)));
  }

  createManager(firstName: string, lastName: string, email: string): Observable<Manager> {
    return this.apiService.post("api/managers/", { firstName, lastName, email })
      .pipe(map((data) => ManagerMapper.fromJson(data)));
  }
}
