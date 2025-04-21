import roleGuard from "./role.guard";
import UserRole from "../domain/user-role.enum";

const adminGuard = roleGuard([UserRole.Admin], "admin/login", "unauthorized")

export default adminGuard;