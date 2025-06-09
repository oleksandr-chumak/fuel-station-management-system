import { Injectable } from '@angular/core';

import { UserRole } from '../models/user-role.enum';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserMapper {
  fromJson(json: unknown): User {  
    const typedJson = json as Record<string, unknown>;
    const role = this.parseUserRole(typedJson['role']);

    return new User(
      Number(typedJson['userId']),
      typedJson['email'] as string,
      role
    );
  }

  private parseUserRole(role: unknown): UserRole {
    if (typeof role === 'string') {
      switch (role) {
      case 'administrator':
        return UserRole.Administrator;
      case 'manager':
        return UserRole.Manager;
      default:
        throw new Error(`Invalid role string: ${role}`);
      }
    } 

    throw new Error(`Unsupported role type: ${typeof role}`);
  }
}