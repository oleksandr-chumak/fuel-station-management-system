import { NgModule, ModuleWithProviders } from '@angular/core';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

import { ManagerApiService } from '../manager/manager-api.service';
import { AuthApiService } from '../auth/auth-api.service';
import { FuelOrderApiService } from '../fuel-order/fuel-order-api.service';
import { FuelStationApiService } from '../fuel-station/fuel-station-api.service';
import { ApiService } from './web-api.service';

import { FuelOrderMapper } from '../fuel-order/fuel-order.mapper';
import { ManagerMapper } from '../manager/manager.mapper';
import { FuelStationMapper } from '../fuel-station/fuel-station.mapper';
import { UserMapper } from '../auth/user.mapper';

@NgModule({
  imports: []
})
export class WebApiModule {
  static forRoot(): ModuleWithProviders<WebApiModule> {
    return {
      ngModule: WebApiModule,
      providers: [
        provideHttpClient(withInterceptorsFromDi()),
        ApiService,
        
        ManagerApiService,
        AuthApiService,
        FuelOrderApiService,
        FuelStationApiService,
        
        FuelOrderMapper,
        ManagerMapper,
        FuelStationMapper,
        UserMapper
      ]
    };
  }
}