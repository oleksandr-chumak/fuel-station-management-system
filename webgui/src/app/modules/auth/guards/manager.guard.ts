import UserRole from '../models/user-role.enum';
import roleGuard from './role.guard';

const managerGuard = roleGuard([UserRole.Manager], 'login', 'unauthorized');

export default managerGuard;