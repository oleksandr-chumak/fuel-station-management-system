import { Routes } from '@angular/router';
import { ManagerDashboardComponent } from './pages/manager-dashboard/manager-dashboard.component';
import { AdminLoginComponent } from './pages/admin/admin-login/admin-login.component';

export const routes: Routes = [
    // manager routes
    { path: "", component: ManagerDashboardComponent },
    // { path: "login", component: ManagerLoginPage },

    // admin routes
    // { path: "admin", component: AdminDashboardPage },
    { path: "admin/login", component: AdminLoginComponent },
];