import { roleGuard } from './role.guard';

import { UserRole } from '~auth/api/models/user-role.enum';

export const managerGuard = roleGuard([UserRole.Manager], 'login', 'unauthorized');
