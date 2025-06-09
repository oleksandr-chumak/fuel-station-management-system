import { UserRole } from '../../api/models/user-role.enum';

import { roleGuard } from './role.guard';

export const adminGuard = roleGuard([UserRole.Administrator], 'admin/login', 'unauthorized');