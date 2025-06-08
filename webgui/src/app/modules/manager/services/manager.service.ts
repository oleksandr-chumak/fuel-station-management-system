import { inject, Injectable } from "@angular/core";
import { ManagerApiService } from "./manager-api.service";
import { Observable, tap } from "rxjs";
import Manager from "../models/manager.model";

@Injectable({ providedIn: "root" })
export default class ManagerService {

  private managerApiService: ManagerApiService = inject(ManagerApiService);

  terminate(manager: Manager): Observable<Manager> {
    return this.managerApiService.terminateManager(manager.id)
      .pipe(tap((m) => m.terminate()));
  }

}