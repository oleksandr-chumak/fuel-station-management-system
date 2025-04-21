import roleGuard from "./role.guard";
import UserRole from "../domain/user-role.enum";

const managerGuard = roleGuard([UserRole.Manager], "login", "unauthorized");

export default managerGuard;