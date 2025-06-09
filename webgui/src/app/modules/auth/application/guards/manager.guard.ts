import { UserRole } from '../../api/models/user-role.enum';

import { roleGuard } from './role.guard';

export const managerGuard = roleGuard([UserRole.Manager], 'login', 'unauthorized');
