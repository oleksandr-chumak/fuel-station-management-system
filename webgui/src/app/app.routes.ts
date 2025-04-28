import { Routes } from '@angular/router';
import { ManagerDashboardComponent } from './pages/manager-dashboard/manager-dashboard.component';
import { AdminLoginComponent } from './pages/admin/admin-login/admin-login.component';
import adminGuard from './modules/auth/infrastructure/admin.guard';
import { FuelStationsAdminComponent } from './pages/admin/fuel-stations-admin/fuel-stations-admin.component';
import { AdminFuelStationComponent } from './pages/admin/admin-fuel-station/admin-fuel-station.component';
import { AdminFuelStationInfoComponent } from './pages/admin/admin-fuel-station/admin-fuel-station-info/admin-fuel-station-info.component';
import { AdminFuelStationManagersComponent } from './pages/admin/admin-fuel-station/admin-fuel-station-managers/admin-fuel-station-managers.component';
import { AdminFuelStationFuelOrdersComponent } from './pages/admin/admin-fuel-station/admin-fuel-station-fuel-orders/admin-fuel-station-fuel-orders.component';
import { AdminManagersComponent } from './pages/admin/admin-managers/admin-managers.component';
import { AdminFuelOrdersComponent } from './pages/admin/admin-fuel-orders/admin-fuel-orders.component';
import { ManagerLoginComponent } from './pages/manager-login/manager-login.component';
import managerGuard from './modules/auth/infrastructure/manager.guard';

export const routes: Routes = [
    // manager routes
    { path: "", component: ManagerDashboardComponent, canActivate: [managerGuard] },
    { path: "login", component: ManagerLoginComponent },

    // admin routes
    { path: "admin/login", component: AdminLoginComponent },
    // TODO rename this component
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
    },
    { path: "admin/managers", component: AdminManagersComponent, canActivate: [adminGuard]},
    { path: "admin/fuel-orders", component: AdminFuelOrdersComponent, canActivate: [adminGuard]}
];