import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  Observable,
  finalize,
  tap,
} from 'rxjs';

import { FuelGrade } from '../../../common/api/fuel-grade.enum';
import { LoadingEvent } from '../../../common/application/loading-event.model'; // Adjust path as needed
import { FuelOrderApiService } from '../../../fuel-order/api/fuel-order-api.service';
import { FuelOrder } from '../../../fuel-order/api/models/fuel-order.model';
import { Manager } from '../../../manager/api/models/manager.model';
import { FuelStationApiService } from '../../api/fuel-station-api.service';
import { FuelStation } from '../../api/models/fuel-station.model';
import { FuelStationContext } from '../models/fuel-station-context.model';
import { ManagerFuelStationContextLoadingEvent } from '../models/manager-fuel-station-context-loading-event.enum';

@Injectable({ providedIn: 'root' })
export class ManagerFuelStationContextService {
  private contextSubject = new BehaviorSubject<FuelStationContext | null>(null);
  context$ = this.contextSubject.asObservable();
  
  private fuelStationApi = inject(FuelStationApiService);
  private fuelOrderApi = inject(FuelOrderApiService);
  
  private loadingEvents = new BehaviorSubject<LoadingEvent<ManagerFuelStationContextLoadingEvent> | null>(null);
  loadingEvents$ = this.loadingEvents.asObservable();
  
  private get contextValue(): FuelStationContext {
    const ctx = this.contextSubject.value;
    if (!ctx) throw new Error('Fuel station context is not initialized.');
    return ctx;
  }
  
  //TODO make as util or smth like that 
  private withLoading<T>(observable: Observable<T>, eventType: ManagerFuelStationContextLoadingEvent): Observable<T> {
    this.loadingEvents.next(new LoadingEvent(eventType, true));
    return observable.pipe(
      finalize(() => this.loadingEvents.next(new LoadingEvent(eventType, false)))
    );
  }
  
  private updateContext(partial: Partial<FuelStationContext>) {
    const current = this.contextValue;
    this.contextSubject.next({ ...current, ...partial });
  }
  
  getContext(): Observable<FuelStationContext | null> {
    return this.context$;
  }
  
  getFuelStation(id: number): Observable<FuelStation> {
    return this.withLoading(
      this.fuelStationApi.getFuelStationById(id).pipe(
        tap(fuelStation =>
          this.contextSubject.next(new FuelStationContext(fuelStation, [], []))
        )
      ),
      ManagerFuelStationContextLoadingEvent.GET_FUEL_STATION
    );
  }
  
  getAssignedManagers(): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;
    return this.withLoading(
      this.fuelStationApi.getAssignedManagers(fuelStation.id).pipe(
        tap(managers => this.updateContext({ managers })),
      ),
      ManagerFuelStationContextLoadingEvent.GET_ASSIGNED_MANAGERS
    );
  }
  
  getFuelOrders(): Observable<FuelOrder[]> {
    const { fuelStation } = this.contextValue;
    return this.withLoading(
      this.fuelStationApi.getFuelStationOrders(fuelStation.id).pipe(
        tap(fuelOrders => this.updateContext({ fuelOrders })),
      ),
      ManagerFuelStationContextLoadingEvent.GET_FUEL_ORDERS
    );
  }
  
  createFuelOrder(fuelGrade: FuelGrade, amount: number): Observable<FuelOrder> {
    const { fuelStation } = this.contextValue;
    return this.withLoading( 
      this.fuelOrderApi.createFuelOrder(fuelStation.id, fuelGrade, amount).pipe(
        tap(() => {
          this.updateContext({ fuelOrders: [] });
          return this.getFuelOrders(); 
        })
      ),
      ManagerFuelStationContextLoadingEvent.CREATE_FUEL_ORDER
    ); 
  }
  
  resetContext(): void {
    this.contextSubject.next(null);
  }
}