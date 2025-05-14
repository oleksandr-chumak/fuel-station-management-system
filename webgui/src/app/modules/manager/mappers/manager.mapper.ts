import { Injectable } from "@angular/core";
import ManagerStatus from "../models/manager-status.enum";
import Manager from "../models/manager.model";

interface ManagerJson {
  id: number;
  firstName: string;
  lastName: string;
  status: string;
}

@Injectable({ providedIn: "root" })
export class ManagerMapper {

  fromJson(json: unknown): Manager {
    if (!this.isManagerJson(json)) {
      throw new Error('Invalid Manager JSON structure');
    }

    const status = this.parseStatus(json.status);

    return new Manager(
      json.id,
      json.firstName,
      json.lastName,
      status
    );
  }

  private isManagerJson(json: unknown): json is ManagerJson {
    if (typeof json !== 'object' || json === null) return false;

    return (
      'id' in json && typeof (json as { id: unknown }).id === 'number' &&
      'firstName' in json && typeof (json as { firstName: unknown }).firstName === 'string' &&
      'lastName' in json && typeof (json as { lastName: unknown }).lastName === 'string' &&
      'status' in json && (typeof (json as { status: unknown }).status === 'string')
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
