import { UserRole } from "fsms-web-api";
import { roleGuard } from "./role.guard";

export const managerGuard = roleGuard([UserRole.Manager], "/login", "/unauthorized");