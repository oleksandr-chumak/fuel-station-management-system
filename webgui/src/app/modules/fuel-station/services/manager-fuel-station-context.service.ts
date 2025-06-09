import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  Observable,
  finalize,
  tap,
} from 'rxjs';

import FuelGrade from '../../common/fuel-grade.enum';
import FuelOrder from '../../fuel-order/models/fuel-order.model';
import { FuelOrderApiService } from '../../fuel-order/services/fuel-order-api.service';
import Manager from '../../manager/models/manager.model';
import FuelStationContext from '../models/fuel-station-context.model';
import { FuelStation } from '../models/fuel-station.model';

import FuelStationApiService from './fuel-station-api.service';

@Injectable({ providedIn: 'root' })
export default class ManagerFuelStationContextService {
  private contextSubject = new BehaviorSubject<FuelStationContext | null>(null);
  context$ = this.contextSubject.asObservable();

  private fuelStationApi = inject(FuelStationApiService);
  private fuelOrderApi = inject(FuelOrderApiService);

  private loading = new BehaviorSubject(false);
  loading$ = this.loading.asObservable();

  private get contextValue(): FuelStationContext {
    const ctx = this.contextSubject.value;
    if (!ctx) throw new Error('Fuel station context is not initialized.');
    return ctx;
  }

  //TODO make as util or smth like that 
  private withLoading<T>(observable: Observable<T>): Observable<T> {
    this.loading.next(true);
    return observable.pipe(finalize(() => this.loading.next(false)))
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
      )
    );
  }

  getAssignedManagers(): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.getAssignedManagers(fuelStation.id).pipe(
        tap(managers => this.updateContext({ managers })),
      )
    );
  }

  getFuelOrders(): Observable<FuelOrder[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.getFuelStationOrders(fuelStation.id).pipe(
        tap(fuelOrders => this.updateContext({ fuelOrders })),
      )
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
      )
    ) 
  }

  resetContext(): void {
    this.contextSubject.next(null);
  }
}
