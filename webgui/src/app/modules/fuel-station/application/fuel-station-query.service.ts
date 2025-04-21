import { inject, Injectable } from "@angular/core";
import QueryService from "../../common/application/query.service";
import FuelStationApiService from "../infrastructure/fuel-station-api.service";

@Injectable({ providedIn: "root" })
export default class FuelStationQueryService {
    private fuelStationApi = inject(FuelStationApiService);

    private queryService = new QueryService(() => this.fuelStationApi.getFuelStations());

    getFuelStations(): void {
        this.queryService.executeQuery()
    }

    get fuelStations$() {
        return this.queryService.data$;
    }

    get loading$() {
        return this.queryService.loading$;
    }

    get error$() {
        return this.queryService.error$;
    }
    
    destroy() {
        this.queryService.destroy();
    }
}