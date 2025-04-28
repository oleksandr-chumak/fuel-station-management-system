import { Transform } from "class-transformer";
import UserRole from "./user-role.enum";

export default class User {

    userId: number;
    email: string;
    @Transform(({ value }) => {
        if (value === "Administrator") return UserRole.Administrator;
        if (value === "Manager") return UserRole.Manager;
        throw new Error("Cannot transform value: " + value + " to UserRole enum")
    })
    role: UserRole;

    constructor(userId: number, email: string, role: UserRole) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    get admin(): boolean {
        return this.role === UserRole.Administrator;
    }

    get manager(): boolean {
        return this.role === UserRole.Manager;
    }

}