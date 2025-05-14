import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router } from "@angular/router";
import { map, take } from "rxjs";
import { AuthService } from "../services/auth.service";
import UserRole from "../models/user-role.enum";

const roleGuard = (
    allowedRoles: UserRole[],
    loginPageUrl: String = "/login", 
    unauthorizedPageUrl: String = "/unauthorized"
) => {
    return (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
      const router = inject(Router);
      const authService = inject(AuthService);
      
      return authService.getUser().pipe(
        take(1),
        map(user => {
          if (!user) {
            router.navigate([loginPageUrl], { queryParams: { returnUrl: state.url } });
            return false;
          }
          
          const hasRole = allowedRoles.some(role => user.role === role);

          if (!hasRole) {
            router.navigate([unauthorizedPageUrl]);
            return false;
          }
          
          return true;
        })
      );
    };
};

export default roleGuard;