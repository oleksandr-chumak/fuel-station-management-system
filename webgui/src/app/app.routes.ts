import { Routes } from '@angular/router';

import { adminGuard } from './modules/auth/application/guards/admin.guard';
import { managerGuard } from './modules/auth/application/guards/manager.guard';
import { AdminFuelOrdersComponent } from './pages/admin/fuel-orders/admin-fuel-orders.component';
import { AdminFuelStationComponent } from './pages/admin/fuel-stations/[id]/admin-fuel-station.component';
import { AdminFuelStationFuelOrdersComponent } from './pages/admin/fuel-stations/[id]/fuel-orders/admin-fuel-station-fuel-orders.component';
import { AdminFuelStationFuelPricesComponent } from './pages/admin/fuel-stations/[id]/fuel-prices/admin-fuel-station-fuel-prices.component';
import { AdminFuelStationFuelTanksComponent } from './pages/admin/fuel-stations/[id]/fuel-tanks/admin-fuel-station-fuel-tanks.component';
import { AdminFuelStationInfoComponent } from './pages/admin/fuel-stations/[id]/info/admin-fuel-station-info.component';
import { AdminFuelStationManagersComponent } from './pages/admin/fuel-stations/[id]/managers/admin-fuel-station-managers.component';
import { FuelStationsAdminComponent } from './pages/admin/fuel-stations/fuel-stations-admin.component';
import { AdminLoginComponent } from './pages/admin/login/admin-login.component';
import { AdminManagersComponent } from './pages/admin/managers/admin-managers.component';
import { FuelStationFuelOrdersComponent } from './pages/fuel-stations/[id]/fuel-orders/fuel-station-fuel-orders.component';
import { FuelStationFuelPricesComponent } from './pages/fuel-stations/[id]/fuel-prices/fuel-station-fuel-prices.component';
import { FuelStationComponent } from './pages/fuel-stations/[id]/fuel-station.component';
import { FuelStationFuelTanksComponent } from './pages/fuel-stations/[id]/fuel-tanks/fuel-station-fuel-tanks.component';
import { FuelStationInfoComponent } from './pages/fuel-stations/[id]/info/fuel-station-info.component';
import { FuelStationManagersComponent } from './pages/fuel-stations/[id]/managers/fuel-station-managers.component';
import { ManagerLoginComponent } from './pages/login/manager-login.component';
import { ManagerDashboardComponent } from './pages/manager-dashboard.component';

export const routes: Routes = [
  // manager routes
  { path: '', component: ManagerDashboardComponent, canActivate: [managerGuard] },
  { path: 'login', component: ManagerLoginComponent },
  { 
    path: 'fuel-station/:id', 
    component: FuelStationComponent, 
    canActivate: [managerGuard],
    children: [
      { path: 'info', component: FuelStationInfoComponent },
      { path: 'managers', component: FuelStationManagersComponent },
      { path: 'fuel-orders', component: FuelStationFuelOrdersComponent },
      { path: 'fuel-tanks', component: FuelStationFuelTanksComponent },
      { path: 'fuel-prices', component: FuelStationFuelPricesComponent },
      { path: '', redirectTo: 'info', pathMatch: 'full' }
    ]
  },

  // admin routes
  { path: 'admin/login', component: AdminLoginComponent },
  // TODO rename this component
  { path: 'admin', component: FuelStationsAdminComponent, canActivate: [adminGuard] },
  { 
    path: 'admin/fuel-station/:id', 
    component: AdminFuelStationComponent, 
    canActivate: [adminGuard],
    children: [
      { path: 'info', component: AdminFuelStationInfoComponent },
      { path: 'managers', component: AdminFuelStationManagersComponent },
      { path: 'fuel-orders', component: AdminFuelStationFuelOrdersComponent },
      { path: 'fuel-tanks', component: AdminFuelStationFuelTanksComponent },
      { path: 'fuel-prices', component: AdminFuelStationFuelPricesComponent },
      { path: '', redirectTo: 'info', pathMatch: 'full' }
    ]
  },
  { path: 'admin/managers', component: AdminManagersComponent, canActivate: [adminGuard]},
  { path: 'admin/fuel-orders', component: AdminFuelOrdersComponent, canActivate: [adminGuard]}
];