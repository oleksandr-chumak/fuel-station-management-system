import { UserRole } from "fsms-web-api";
import roleGuard from "./role.guard";

const managerGuard = roleGuard([UserRole.Manager], "login", "unauthorized");

export default managerGuard;