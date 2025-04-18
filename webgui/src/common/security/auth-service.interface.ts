import User from "./user.model";

export default interface IAuthService {
    getUser(): User | null;
    getAccessToken(): String | null;
    isAuthorized(): boolean;
    isAdmin(): boolean; 
    isManager(): boolean;
    loginAdmin(email: string, password: string): void;
    loginManager(email: string, password: string): void;
    logout(): void;
};