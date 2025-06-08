import { inject, Injectable } from "@angular/core";
import QueryService from "../../common/query.service";
import FuelStationApiService from "./fuel-station-api.service";
import { Observable } from "rxjs";
import { FuelStation } from "../models/fuel-station.model";

@Injectable({ providedIn: "root" })
export default class FuelStationsQueryService {
  private fuelStationApi = inject(FuelStationApiService);

  private queryService = new QueryService(() => this.fuelStationApi.getFuelStations());

  getFuelStations(): Observable<FuelStation[]> {
    return this.queryService.executeQuery()
  }

  get fuelStations$() {
    return this.queryService.data$;
  }

  get loading$() {
    return this.queryService.loading$;
  }
    
  clear() {
    this.queryService.clearQuery();
  }
}