import { Injectable } from "@angular/core"; 
import { Manager } from "./manager.model";
import { ManagerStatus } from "./manager-status.enum";

@Injectable({ providedIn: "root" })
export class ManagerMapper {

  fromJson(json: unknown): Manager {
    const status = this.parseStatus((json as Manager).status);

    return new Manager(
      (json as Manager).managerId,
      (json as Manager).firstName,
      (json as Manager).lastName,
      status
    );
  }

  private parseStatus(status: unknown): ManagerStatus {
    if (typeof status === 'string') {
      switch(status) {
        case "active": 
          return ManagerStatus.Active;
        case "deactivated":
          return ManagerStatus.Deactivated;
        default:
          throw new Error(`Unsupported status type: ${typeof status}`);
      }
    }

    throw new Error(`Unsupported status type: ${typeof status}`);
  }
}
