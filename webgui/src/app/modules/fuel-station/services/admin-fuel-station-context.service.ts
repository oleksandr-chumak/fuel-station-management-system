import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  Observable,
  finalize,
  switchMap,
  tap,
} from 'rxjs';

import { FuelGrade } from '../../common/fuel-grade.enum';
import { FuelOrder } from '../../fuel-order/models/fuel-order.model';
import { FuelOrderApiService } from '../../fuel-order/services/fuel-order-api.service';
import { Manager } from '../../manager/models/manager.model';
import { FuelStationContext } from '../models/fuel-station-context.model';
import { FuelStation } from '../models/fuel-station.model';

import { FuelStationApiService } from './fuel-station-api.service';

@Injectable({ providedIn: 'root' })
export class AdminFuelStationContextService {
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

  private withLoading<T>(observable: Observable<T>): Observable<T> {
    this.loading.next(true);
    return observable.pipe(finalize(() => this.loading.next(false)));
  }

  private updateContext(partial: Partial<FuelStationContext>) {
    const current = this.contextValue;
    this.contextSubject.next({ ...current, ...partial });
  }

  getContext(): Observable<FuelStationContext | null> {
    return this.context$;
  }

  getContextValue(): FuelStationContext | null {
    return this.contextSubject.value;
  }

  getFuelStation(id: number): Observable<FuelStation> {
    return this.withLoading(
      this.fuelStationApi.getFuelStationById(id)
        .pipe(
          tap(fuelStation => this.contextSubject.next(new FuelStationContext(fuelStation, [], [])))
        )
    );
  }

  getAssignedManagers(): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.getAssignedManagers(fuelStation.id)
        .pipe(
          tap(managers => this.updateContext({ managers })),
        )
    );
  }

  getFuelOrders(): Observable<FuelOrder[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.getFuelStationOrders(fuelStation.id)
        .pipe(
          tap(fuelOrders => this.updateContext({ fuelOrders })),
        )
    );
  }

  assignManager(managerId: number): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.assignManager(fuelStation.id, managerId)
        .pipe(
          switchMap(() => {
            this.updateContext({ managers: [] });
            return this.getAssignedManagers();
          })
        )
    );
  }

  unassignManager(managerId: number): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.unassignManager(fuelStation.id, managerId)
        .pipe(
          switchMap(() => {
            this.updateContext({ managers: [] });
            return this.getAssignedManagers();
          })
        )
    );
  }

  confirmFuelOrder(fuelOrderId: number): Observable<FuelOrder> {
    // eslint-disable-next-line @typescript-eslint/no-unused-expressions
    this.contextValue;
    return this.withLoading(
      this.fuelOrderApi.confirmFuelOrder(fuelOrderId).pipe(
        tap(() => {
          this.updateContext({ fuelOrders: [] });
          return this.getFuelOrders();
        })
      )
    );
  }

  rejectFuelOrder(fuelOrderId: number): Observable<FuelOrder> {
    // eslint-disable-next-line @typescript-eslint/no-unused-expressions
    this.contextValue;
    return this.withLoading(
      this.fuelOrderApi.rejectFuelOrder(fuelOrderId).pipe(
        tap(() => {
          this.updateContext({ fuelOrders: [] });
          this.getFuelOrders();
        }),
      )
    );
  }

  changeFuelPrice(fuelGrade: FuelGrade, newPrice: number): Observable<FuelStation> {
    const { fuelStation, managers, fuelOrders } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.changeFuelPrice(fuelStation.id, fuelGrade, newPrice)
        .pipe(
          tap(updatedStation => {
            this.contextSubject.next(new FuelStationContext(updatedStation, managers, fuelOrders));
          }),
        )
    );
  }

  deactivateFuelStation(): Observable<FuelStation> {
    const { fuelStation, managers, fuelOrders } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.deactivateFuelStation(fuelStation.id)
        .pipe(
          tap(updatedStation => {
            this.contextSubject.next(new FuelStationContext(updatedStation, managers, fuelOrders));
          })
        )
    );
  }

  resetContext(): void {
    this.contextSubject.next(null);
  }
}
