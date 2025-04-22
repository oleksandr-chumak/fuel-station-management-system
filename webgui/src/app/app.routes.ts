import { Routes } from '@angular/router';
import { ManagerDashboardComponent } from './pages/manager-dashboard/manager-dashboard.component';
import { AdminLoginComponent } from './pages/admin/admin-login/admin-login.component';
import adminGuard from './modules/auth/infrastructure/admin.guard';
import { FuelStationsAdminComponent } from './pages/admin/fuel-stations-admin/fuel-stations-admin.component';
import { AdminFuelStationComponent } from './pages/admin/admin-fuel-station/admin-fuel-station.component';
import { AdminFuelStationInfoComponent } from './pages/admin/admin-fuel-station/admin-fuel-station-info/admin-fuel-station-info.component';
import { AdminFuelStationManagersComponent } from './pages/admin/admin-fuel-station/admin-fuel-station-managers/admin-fuel-station-managers.component';
import { AdminFuelStationFuelOrdersComponent } from './pages/admin/admin-fuel-station/admin-fuel-station-fuel-orders/admin-fuel-station-fuel-orders.component';

export const routes: Routes = [
    // manager routes
    { path: "", component: ManagerDashboardComponent },
    // { path: "login", component: ManagerLoginPage },

    // admin routes
    { path: "admin/login", component: AdminLoginComponent },
    { path: "admin", component: FuelStationsAdminComponent, canActivate: [adminGuard] },

    { 
        path: "admin/fuel-station/:id", 
        component: AdminFuelStationComponent, 
        canActivate: [adminGuard],
        children: [
            { path: "info", component: AdminFuelStationInfoComponent },
            { path: "managers", component: AdminFuelStationManagersComponent },
            { path: "fuel-orders", component: AdminFuelStationFuelOrdersComponent },
            { path: "", redirectTo: "info", pathMatch: "full" }
        ]
    }
];