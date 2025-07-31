import { roleGuard } from './role.guard';

import { UserRole } from '~auth/api/models/user-role.enum';

export const adminGuard = roleGuard([UserRole.Administrator], 'admin/login', 'unauthorized');