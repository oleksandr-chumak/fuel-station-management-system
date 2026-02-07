import { Routes } from '@angular/router';
import managerGuard from './modules/auth/guards/manager.guard';
import { DashboardPage } from './pages/dashboard/dashboard.page';
import { LoginPage } from './pages/login/login.page';
import { FuelStationPage } from './pages/fuel-stations/[id]/fuel-station.page';
import { FuelStationInfoPage } from './pages/fuel-stations/[id]/fuel-station-info/fuel-station-info.page';
import { FuelStationManagersPage } from './pages/fuel-stations/[id]/fuel-station-managers/fuel-station-managers.page';
import { FuelStationFuelOrdersPage } from './pages/fuel-stations/[id]/fuel-station-fuel-orders/fuel-station-fuel-orders.page';
import { FuelStationFuelTanksPage } from './pages/fuel-stations/[id]/fuel-station-fuel-tanks/fuel-station-fuel-tanks.page';
import { FuelStationFuelPricesPage } from './pages/fuel-stations/[id]/fuel-station-fuel-prices/fuel-station-fuel-prices.page';

export const routes: Routes = [
    { path: "", component: DashboardPage, canActivate: [managerGuard] },
    { path: "login", component: LoginPage },
    { 
        path: "fuel-station/:id", 
        component: FuelStationPage, 
        canActivate: [managerGuard],
        children: [
            { path: "info", component: FuelStationInfoPage },
            { path: "managers", component: FuelStationManagersPage },
            { path: "fuel-orders", component: FuelStationFuelOrdersPage },
            { path: "fuel-tanks", component: FuelStationFuelTanksPage },
            { path: "fuel-prices", component: FuelStationFuelPricesPage },
            { path: "", redirectTo: "info", pathMatch: "full" }
        ]
    },
];