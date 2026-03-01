import { Injectable } from "@angular/core";
import { User } from "./user.model";
import { UserRole } from "./user-role.enum";

@Injectable({ providedIn: "root" })
export class UserMapper {

  fromJson(json: unknown): User {  
    const role = this.parseUserRole((json as User).role);

    return new User(
      (json as User).userId,
      (json as User).firstName,
      (json as User).lastName,
      (json as User).fullName,
      (json as User).email,
      role
    );
  }

  private parseUserRole(role: string | number): UserRole {
    if (typeof role === 'string') {
      switch (role) {
        case "administrator":
          return UserRole.Administrator;
        case "manager":
          return UserRole.Manager;
        default:
          throw new Error(`Invalid role string: ${role}`);
      }
    } 

    throw new Error(`Unsupported role type: ${typeof role}`);
  }
}