import UserRole from '../models/user-role.enum';
import roleGuard from './role.guard';

const adminGuard = roleGuard([UserRole.Administrator], 'admin/login', 'unauthorized')

export default adminGuard;