import { UserRole } from "./user-role.enum";

export class User {

    constructor(
        public userId: number, 
        public firstName: string,
        public lastName: string,
        public fullName: string,
        public email: string, 
        public role: UserRole
    ) {}

    get admin(): boolean {
        return this.role === UserRole.Administrator;
    }

    get manager(): boolean {
        return this.role === UserRole.Manager;
    }

}