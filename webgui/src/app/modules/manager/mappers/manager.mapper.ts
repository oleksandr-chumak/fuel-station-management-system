import ManagerStatus from "../models/manager-status.enum";
import Manager from "../models/manager.model";

export class ManagerMapper {

  static fromJson(json: unknown): Manager {
    const typedJson = json as Record<string, unknown>
    const status = this.parseStatus(typedJson['status']);

    return new Manager(
      Number(typedJson['id']),
      typedJson['firstName'] as string,
      typedJson['lastName'] as string,
      status
    );
  }

  private static parseStatus(status: unknown): ManagerStatus {
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
