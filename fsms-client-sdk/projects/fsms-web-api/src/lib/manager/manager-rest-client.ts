import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { RestClient } from "../core/rest-client";
import { ManagerMapper } from "./manager.mapper";
import { FuelStationMapper } from "../fuel-station/fuel-station.mapper";
import { Manager } from "./manager.model";
import { FuelStation } from "../fuel-station/fuel-station.model";

@Injectable({ providedIn: "root" })
export class ManagerRestClient {

  private restClient = inject(RestClient);
  private managerMapper = inject(ManagerMapper);
  private fuelStationMapper = inject(FuelStationMapper);

  getManagerById(managerId: number): Observable<Manager> {
    return this.restClient.get("api/managers/" + managerId)
      .pipe(map(this.managerMapper.fromJson));
  }

  getManagers(): Observable<Manager[]> {
    return this.restClient.get("api/managers/")
      .pipe(map(data => this.restClient.assertArray(data, this.managerMapper.fromJson.bind(this.managerMapper))));
  }

  getManagerFuelStations(managerId: number): Observable<FuelStation[]> {
    return this.restClient.get("api/managers/" + managerId + "/fuel-stations")
      .pipe(map(data => this.restClient.assertArray(data, this.fuelStationMapper.fromJson.bind(this.fuelStationMapper))));
  }

  terminateManager(managerId: number): Observable<Manager> {
    return this.restClient.put("api/managers/" + managerId)
      .pipe(map((data) => this.managerMapper.fromJson(data)));
  }

  createManager(firstName: string, lastName: string, email: string): Observable<Manager> {
    return this.restClient.post("api/managers/", { firstName, lastName, email })
      .pipe(map((data) => this.managerMapper.fromJson(data)));
  }

}
