import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  Observable,
  finalize,
  switchMap,
  tap,
} from 'rxjs';

import { FuelGrade } from '~common/api/fuel-grade.enum';
import { LoadingEvent } from '~common/application/loading-event.model'; // Adjust path as needed
import { FuelOrderApiService } from '~fuel-order/api/fuel-order-api.service';
import { FuelOrder } from '~fuel-order/api/models/fuel-order.model';
import { FuelStationApiService } from '~fuel-station/api/fuel-station-api.service';
import { FuelStation } from '~fuel-station/api/models/fuel-station.model';
import { AdminFuelStationContextLoadingEvent } from '~fuel-station/application/models/admin-fuel-station-context-loading-event.enum';
import { FuelStationContext } from '~fuel-station/application/models/fuel-station-context.model';
import { Manager } from '~manager/api/models/manager.model';

@Injectable({ providedIn: 'root' })
export class AdminFuelStationContextService {
  private contextSubject = new BehaviorSubject<FuelStationContext | null>(null);
  context$ = this.contextSubject.asObservable();

  private fuelStationApi = inject(FuelStationApiService);
  private fuelOrderApi = inject(FuelOrderApiService);

  private loadingEvents = new BehaviorSubject<LoadingEvent<AdminFuelStationContextLoadingEvent> | null>(null);
  loadingEvents$ = this.loadingEvents.asObservable();

  private get contextValue(): FuelStationContext {
    const ctx = this.contextSubject.value;
    if (!ctx) throw new Error('Fuel station context is not initialized.');
    return ctx;
  }

  private withLoading<T>(observable: Observable<T>, eventType: AdminFuelStationContextLoadingEvent): Observable<T> {
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

  getContextValue(): FuelStationContext | null {
    return this.contextSubject.value;
  }

  getFuelStation(id: number): Observable<FuelStation> {
    return this.withLoading(
      this.fuelStationApi.getFuelStationById(id)
        .pipe(
          tap(fuelStation => this.contextSubject.next(new FuelStationContext(fuelStation, [], [])))
        ),
      AdminFuelStationContextLoadingEvent.GET_FUEL_STATION
    );
  }

  getAssignedManagers(): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.getAssignedManagers(fuelStation.id)
        .pipe(
          tap(managers => this.updateContext({ managers })),
        ),
      AdminFuelStationContextLoadingEvent.GET_ASSIGNED_MANAGERS
    );
  }

  getFuelOrders(): Observable<FuelOrder[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      this.fuelStationApi.getFuelStationOrders(fuelStation.id)
        .pipe(
          tap(fuelOrders => this.updateContext({ fuelOrders })),
        ),
      AdminFuelStationContextLoadingEvent.GET_FUEL_ORDERS
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
        ),
      AdminFuelStationContextLoadingEvent.ASSIGN_MANAGER
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
        ),
      AdminFuelStationContextLoadingEvent.UNASSIGN_MANAGER
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
      ),
      AdminFuelStationContextLoadingEvent.CONFIRM_FUEL_ORDER
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
      ),
      AdminFuelStationContextLoadingEvent.REJECT_FUEL_ORDER
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
        ),
      AdminFuelStationContextLoadingEvent.CHANGE_FUEL_PRICE
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
        ),
      AdminFuelStationContextLoadingEvent.DEACTIVATE_FUEL_STATION
    );
  }

  resetContext(): void {
    this.contextSubject.next(null);
  }
}