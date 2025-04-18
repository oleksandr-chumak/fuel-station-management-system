import UserRole from "./user.enum";

export default class User {
    email: string;
    role: UserRole;

    constructor(email: string, role: UserRole) {
        this.email = email;
        this.role = role;
    }

}