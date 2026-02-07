import { UserRole } from "fsms-web-api";
import { roleGuard } from "./role.guard";

export const adminGuard = roleGuard([UserRole.Administrator], "/login", "/unauthorized")