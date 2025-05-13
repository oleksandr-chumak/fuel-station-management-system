import { inject, Injectable } from "@angular/core";
import QueryService from "../../common/query.service";
import { map } from "rxjs";
import { ManagerApiService } from "./manager-api.service";

@Injectable({ providedIn: "root" })
export default class ManagersQueryService {

    private managerApiService: ManagerApiService = inject(ManagerApiService);
    private queryService = new QueryService(() => this.managerApiService.getManagers());

    getManagers() {
        return this.queryService.executeQuery();
    }

    get managers$() {
        return this.queryService.data$.pipe(map(data => data ? data : []));
    }

    get loading$() {
        return this.queryService.loading$;
    }

}