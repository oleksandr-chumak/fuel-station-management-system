export enum UserRole {
    Manager,
    Admin
}

export interface User {
    email: string;
    role: UserRole;
}