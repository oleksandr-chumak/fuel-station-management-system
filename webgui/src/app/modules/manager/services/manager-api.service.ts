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
  private managerMapper = inject(ManagerMapper);
  private fuelStationMapper = inject(FuelStationMapper);

  getManagerById(managerId: number): Observable<Manager> {
    return this.apiService.get("api/managers/" + managerId)
      .pipe(map(this.managerMapper.fromJson));
  }

  getManagers(): Observable<Manager[]> {
    return this.apiService.get("api/managers/")
      .pipe(map(data => this.apiService.assertArray(data, this.managerMapper.fromJson.bind(this.managerMapper))));
  }

  getManagerFuelStations(managerId: number): Observable<FuelStation[]> {
    return this.apiService.get("api/managers/" + managerId + "/fuel-stations")
      .pipe(map(data => this.apiService.assertArray(data, this.fuelStationMapper.fromJson.bind(this.managerMapper))));
  }

  terminateManager(managerId: number): Observable<Manager> {
    return this.apiService.put("api/managers/" + managerId)
      .pipe(map(this.managerMapper.fromJson));
  }

  createManager(firstName: string, lastName: string, email: string): Observable<Manager> {
    return this.apiService.post("api/managers/", { firstName, lastName, email })
      .pipe(map(this.managerMapper.fromJson));
  }
}
