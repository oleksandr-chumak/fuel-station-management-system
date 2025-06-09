import { UserRole } from '../models/user-role.enum';

import { roleGuard } from './role.guard';

export const managerGuard = roleGuard([UserRole.Manager], 'login', 'unauthorized');
