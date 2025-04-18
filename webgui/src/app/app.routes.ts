import { Routes } from '@angular/router';
import { ManagerDashboardPage, ManagerLoginPage } from './pages/manager';
import { AdminDashboardPage, AdminLoginPage } from './pages/admin';

export const routes: Routes = [
    // manager routes
    { path: "", component: ManagerDashboardPage },
    { path: "login", component: ManagerLoginPage },

    // admin routes
    { path: "admin", component: AdminDashboardPage },
    { path: "admin/login", component: AdminLoginPage },
];