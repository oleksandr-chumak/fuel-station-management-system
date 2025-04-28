import roleGuard from "./role.guard";
import UserRole from "../domain/user-role.enum";

const adminGuard = roleGuard([UserRole.Administrator], "admin/login", "unauthorized")

export default adminGuard;