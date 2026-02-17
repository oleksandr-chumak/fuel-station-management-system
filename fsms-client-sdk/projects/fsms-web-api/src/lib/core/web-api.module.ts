import { NgModule, ModuleWithProviders } from '@angular/core';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

import { ManagerRestClient } from '../manager/manager-rest-client';
import { AuthRestClient } from '../auth/auth-rest-client';
import { FuelOrderRestClient } from '../fuel-order/fuel-order-rest-client';
import { FuelStationRestClient } from '../fuel-station/fuel-station-rest-client';
import { RestClient } from './rest-client';

import { FuelOrderMapper } from '../fuel-order/fuel-order.mapper';
import { ManagerMapper } from '../manager/manager.mapper';
import { FuelStationMapper } from '../fuel-station/fuel-station.mapper';
import { UserMapper } from '../auth/user.mapper';
import { StompClient } from './stomp-client';
import { FuelStationStompClient } from '../fuel-station/fuel-station-stomp-client';

@NgModule({
  imports: []
})
export class WebApiModule {
  static forRoot(): ModuleWithProviders<WebApiModule> {
    return {
      ngModule: WebApiModule,
      providers: [
        provideHttpClient(withInterceptorsFromDi()),
        StompClient,
        RestClient,
        
        ManagerRestClient,
        AuthRestClient,
        FuelOrderRestClient,
        FuelStationRestClient,
        FuelStationStompClient,
        
        FuelOrderMapper,
        ManagerMapper,
        FuelStationMapper,
        UserMapper
      ]
    };
  }
}