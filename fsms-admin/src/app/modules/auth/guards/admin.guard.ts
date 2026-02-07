import { UserRole } from "fsms-web-api";
import roleGuard from "./role.guard";

const adminGuard = roleGuard([UserRole.Administrator], "admin/login", "unauthorized")

export default adminGuard;