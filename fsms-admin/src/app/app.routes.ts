import { Routes } from '@angular/router';
import adminGuard from './modules/auth/guards/admin.guard';
import { FuelStationPage } from './pages/fuel-stations/[id]/fuel-station.page';
import { LoginPage } from './pages/login/login.page';
import { FuelStationsPage } from './pages/fuel-stations/fuel-stations-admin.component';
import { FuelStationInfoPage } from './pages/fuel-stations/[id]/fuel-station-info/fuel-station-info.page';
import { FuelStationManagersPage } from './pages/fuel-stations/[id]/fuel-station-managers/fuel-station-managers.page';
import { FuelStationFuelOrdersPage } from './pages/fuel-stations/[id]/fuel-station-fuel-orders/fuel-station-fuel-orders.page';
import { FuelStationFuelTanksPage } from './pages/fuel-stations/[id]/fuel-station-fuel-tanks/fuel-station-fuel-tanks.page';
import { FuelStationFuelPricesPage } from './pages/fuel-stations/[id]/fuel-station-fuel-prices/fuel-station-fuel-prices.page';
import { ManagersPage } from './pages/managers/managers.page';
import { FuelOrdersPage } from './pages/fuel-orders/fuel-orders.component';

export const routes: Routes = [
    { path: "fuel-stations", component: FuelStationsPage, canActivate: [adminGuard] },
    { path: "login", component: LoginPage },
    { 
        path: "fuel-stations/:id", 
        component: FuelStationPage, 
        canActivate: [adminGuard],
        children: [
            { path: "info", component: FuelStationInfoPage },
            { path: "managers", component: FuelStationManagersPage },
            { path: "fuel-orders", component: FuelStationFuelOrdersPage },
            { path: "fuel-tanks", component: FuelStationFuelTanksPage },
            { path: "fuel-prices", component: FuelStationFuelPricesPage },
            { path: "", redirectTo: "info", pathMatch: "full" }
        ]
    },
    { path: "managers", component: ManagersPage, canActivate: [adminGuard]},
    { path: "fuel-orders", component: FuelOrdersPage, canActivate: [adminGuard]}
];