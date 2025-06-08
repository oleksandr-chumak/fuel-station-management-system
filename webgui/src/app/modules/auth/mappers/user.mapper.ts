import { Injectable } from "@angular/core";
import User from "../models/user.model";
import UserRole from "../models/user-role.enum";

interface UserJson {
  userId: number;
  email: string;
  role: string | number;
}

@Injectable({ providedIn: "root" })
export class UserMapper {

  fromJson(json: unknown): User {  
    if (!this.isUserJson(json)) {
      throw new Error('Invalid User JSON structure');
    }

    const role = this.parseUserRole(json.role);

    return new User(
      json.userId,
      json.email,
      role
    );
  }

  private isUserJson(json: unknown): json is UserJson {
    if (typeof json !== 'object' || json === null) return false;

    const typedJson = json as Partial<UserJson>;
    
    return (
      'userId' in json && typeof typedJson.userId === 'number' &&
      'email' in json && typeof typedJson.email === 'string' &&
      'role' in json && typeof typedJson.role === 'string'
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