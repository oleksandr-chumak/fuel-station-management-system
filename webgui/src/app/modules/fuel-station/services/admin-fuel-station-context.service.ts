import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  Observable,
  catchError,
  finalize,
  switchMap,
  tap,
  throwError
} from 'rxjs';

import FuelGrade from '../../common/fuel-grade.enum';
import FuelOrder from '../../fuel-order/models/fuel-order.model';
import { FuelOrderApiService } from '../../fuel-order/services/fuel-order-api.service';
import Manager from '../../manager/models/manager.model';
import FuelStationContext from '../models/fuel-station-context.model';
import { FuelStation } from '../models/fuel-station.model';

import FuelStationApiService from './fuel-station-api.service';

@Injectable({ providedIn: 'root' })
export default class AdminFuelStationContextService {
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
      this.fuelStationApi.getFuelStationById(id).pipe(
        tap(fuelStation =>
          this.contextSubject.next(new FuelStationContext(fuelStation, [], []))
        ),
        catchError(error => {
          console.error('Error fetching fuel station:', error);
          return throwError(() => new Error(`Failed to fetch fuel station with ID ${id}`));
        })
      )
    );
  }

  getAssignedManagers(): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.getAssignedManagers(fuelStation.id).pipe(
        tap(managers => this.updateContext({ managers })),
        catchError(error => {
          console.error('Error fetching managers:', error);
          return throwError(() => new Error('Failed to fetch assigned managers'));
        })
      )
    );
  }

  getFuelOrders(): Observable<FuelOrder[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.getFuelStationOrders(fuelStation.id).pipe(
        tap(fuelOrders => this.updateContext({ fuelOrders })),
        catchError(error => {
          console.error('Error fetching orders:', error);
          return throwError(() => new Error('Failed to fetch fuel orders'));
        })
      )
    );
  }

  assignManager(managerId: number): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.assignManager(fuelStation.id, managerId).pipe(
        switchMap(() => {
          this.updateContext({ managers: [] })
          return this.getAssignedManagers()
        }),
        catchError(error => {
          console.error('Error assigning manager:', error);
          return throwError(() => new Error(`Failed to assign manager with ID ${managerId}`));
        })
      )
    );
  }

  unassignManager(managerId: number): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.unassignManager(fuelStation.id, managerId).pipe(
        switchMap(() => {
          this.updateContext({ managers: [] })
          return this.getAssignedManagers()
        }),
        catchError(error => {
          console.error('Error unassigning manager:', error);
          return throwError(() => new Error(`Failed to unassign manager with ID ${managerId}`));
        })
      )
    );
  }

  confirmFuelOrder(fuelOrderId: number): Observable<FuelOrder[]> {
    // eslint-disable-next-line @typescript-eslint/no-unused-expressions
    this.contextValue;
    return this.withLoading(
      this.fuelOrderApi.confirmFuelOrder(fuelOrderId).pipe(
        switchMap(() => {
          this.updateContext({ fuelOrders: [] })
          return this.getFuelOrders();
        }),
        catchError(error => {
          console.error('Error confirming fuel order:', error);
          return throwError(() => new Error(`Failed to confirm fuel order with ID ${fuelOrderId}`));
        })
      )
    );
  }

  rejectFuelOrder(fuelOrderId: number): Observable<FuelOrder[]> {
    // eslint-disable-next-line @typescript-eslint/no-unused-expressions
    this.contextValue;
    return this.withLoading(
      this.fuelOrderApi.rejectFuelOrder(fuelOrderId).pipe(
        switchMap(() => {
          this.updateContext({ fuelOrders: [] })
          return this.getFuelOrders()
        }),
        catchError(error => {
          console.error('Error rejecting fuel order:', error);
          return throwError(() => new Error(`Failed to reject fuel order with ID ${fuelOrderId}`));
        })
      )
    );
  }

  changeFuelPrice(fuelGrade: FuelGrade, newPrice: number): Observable<FuelStation> {
    const { fuelStation, managers, fuelOrders } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.changeFuelPrice(fuelStation.id, fuelGrade, newPrice).pipe(
        tap(updatedStation => {
          this.contextSubject.next(new FuelStationContext(updatedStation, managers, fuelOrders));
        }),
        catchError(error => {
          console.error('Error changing fuel price:', error);
          return throwError(() => new Error(`Failed to change fuel price for grade ${fuelGrade}`));
        })
      )
    );
  }

  deactivateFuelStation(): Observable<FuelStation> {
    const { fuelStation, managers, fuelOrders } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.deactivateFuelStation(fuelStation.id).pipe(
        tap(updatedStation => {
          this.contextSubject.next(new FuelStationContext(updatedStation, managers, fuelOrders));
        }),
        catchError(error => {
          console.error('Error deactivating fuel station:', error);
          return throwError(() => new Error('Failed to deactivate fuel station'));
        })
      )
    );
  }

  resetContext(): void {
    this.contextSubject.next(null);
  }
}
