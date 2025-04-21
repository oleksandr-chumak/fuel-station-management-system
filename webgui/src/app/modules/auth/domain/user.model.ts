import UserRole from "./user-role.enum";

export default class User {
    constructor(
        public email: string, 
        public role: UserRole
    ) {}

    get admin(): boolean {
        return this.role == UserRole.Admin;
    }

    get manager(): boolean {
        return this.role == UserRole.Manager;
    }
}