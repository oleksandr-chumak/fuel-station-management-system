import { inject, Injectable } from "@angular/core";
import { ManagerApiService } from "../infrastructure/manager-api.service";
import QueryService from "../../common/application/query.service";
import { map } from "rxjs";

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