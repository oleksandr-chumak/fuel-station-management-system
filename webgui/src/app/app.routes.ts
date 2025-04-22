import { Routes } from '@angular/router';
import { ManagerDashboardComponent } from './pages/manager-dashboard/manager-dashboard.component';
import { AdminLoginComponent } from './pages/admin/admin-login/admin-login.component';
import adminGuard from './modules/auth/infrastructure/admin.guard';
import { FuelStationsAdminComponent } from './pages/admin/fuel-stations-admin/fuel-stations-admin.component';

export const routes: Routes = [
    // manager routes
    { path: "", component: ManagerDashboardComponent },
    // { path: "login", component: ManagerLoginPage },

    // admin routes
    { path: "admin", component: FuelStationsAdminComponent, canActivate: [adminGuard] },
    { path: "admin/login", component: AdminLoginComponent },
];