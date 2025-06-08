import { inject, Injectable } from "@angular/core";
import {
  BehaviorSubject,
  Observable,
  catchError,
  finalize,
  switchMap,
  tap,
  throwError
} from "rxjs";

import FuelStationApiService from "./fuel-station-api.service";
import Manager from "../../manager/models/manager.model";
import FuelOrder from "../../fuel-order/models/fuel-order.model";
import FuelGrade from "../../common/fuel-grade.enum";
import { FuelOrderApiService } from "../../fuel-order/services/fuel-order-api.service";
import FuelStationContext from "../models/fuel-station-context.model";
import { FuelStation } from "../models/fuel-station.model";

type LoadingKey = 'fuelStation' | 'managers' | 'fuelOrders' | 'createFuelOrder';

@Injectable({ providedIn: "root" })
export default class ManagerFuelStationContextService {
  private contextSubject = new BehaviorSubject<FuelStationContext | null>(null);
  context$ = this.contextSubject.asObservable();

  private fuelStationApi = inject(FuelStationApiService);
  private fuelOrderApi = inject(FuelOrderApiService);

  private loadingSubjects: Record<LoadingKey, BehaviorSubject<boolean>> = {
    fuelStation: new BehaviorSubject<boolean>(false),
    managers: new BehaviorSubject<boolean>(false),
    fuelOrders: new BehaviorSubject<boolean>(false),
    createFuelOrder: new BehaviorSubject<boolean>(false)
  };

  loading = {
    fuelStation: this.loadingSubjects.fuelStation.asObservable(),
    managers: this.loadingSubjects.managers.asObservable(),
    fuelOrders: this.loadingSubjects.fuelOrders.asObservable(),
    createFuelOrder: this.loadingSubjects.createFuelOrder.asObservable()
  };

  private get contextValue(): FuelStationContext {
    const ctx = this.contextSubject.value;
    if (!ctx) throw new Error("Fuel station context is not initialized.");
    return ctx;
  }

  private withLoading<T>(key: LoadingKey, observable: Observable<T>): Observable<T> {
    const subject = this.loadingSubjects[key];
    subject?.next(true);
    return observable.pipe(finalize(() => subject?.next(false)));
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
      "fuelStation",
      this.fuelStationApi.getFuelStationById(id).pipe(
        tap(fuelStation =>
          this.contextSubject.next(new FuelStationContext(fuelStation, [], []))
        ),
        catchError(error => {
          console.error("Error fetching fuel station:", error);
          return throwError(() => new Error(`Failed to fetch fuel station with ID ${id}`));
        })
      )
    );
  }

  getAssignedManagers(): Observable<Manager[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      "managers",
      this.fuelStationApi.getAssignedManagers(fuelStation.id).pipe(
        tap(managers => this.updateContext({ managers })),
        catchError(error => {
          console.error("Error fetching managers:", error);
          return throwError(() => new Error("Failed to fetch assigned managers"));
        })
      )
    );
  }

  getFuelOrders(): Observable<FuelOrder[]> {
    const { fuelStation } = this.contextValue;

    return this.withLoading(
      "fuelOrders",
      this.fuelStationApi.getFuelStationOrders(fuelStation.id).pipe(
        tap(console.log),
        tap(fuelOrders => this.updateContext({ fuelOrders })),
        catchError(error => {
          console.error("Error fetching orders:", error);
          return throwError(() => new Error("Failed to fetch fuel orders"));
        })
      )
    );
  }

  createFuelOrder(fuelGrade: FuelGrade, amount: number): Observable<FuelOrder[]> {
    const { fuelStation } = this.contextValue;
    return this.withLoading("createFuelOrder", 
      this.fuelOrderApi.createFuelOrder(fuelStation.id, fuelGrade, amount).pipe(
        // TODO refactor to tap? 
        switchMap(() => {
          this.updateContext({ fuelOrders: [] });
          return this.getFuelOrders(); 
        }),
        catchError(error => {
          console.error("Error creating fuel order", error);
          return throwError(() => new Error("Failed to create fuel order"));
        })
      )
    ) 
  }

  resetContext(): void {
    this.contextSubject.next(null);
  }

}
