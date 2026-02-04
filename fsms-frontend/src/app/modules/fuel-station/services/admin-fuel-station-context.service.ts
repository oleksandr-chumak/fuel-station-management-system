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

import FuelStationContext from "../models/fuel-station-context.model";
import { FuelStationApiService, FuelOrderApiService, FuelStation, Manager, FuelOrder, FuelGrade } from "fsms-web-api";

type LoadingKey = 'fuelStation' | 'managers' | 'fuelOrders' | 'assignManager' | 'unassignManager' | 'confirmOrder' | 'rejectOrder' | 'changeFuelPrice' | 'deactivateFuelStation';

@Injectable({ providedIn: "root" })
export default class AdminFuelStationContextService {
    private contextSubject = new BehaviorSubject<FuelStationContext | null>(null);
    context$ = this.contextSubject.asObservable();

    private fuelStationApi = inject(FuelStationApiService);
    private fuelOrderApi = inject(FuelOrderApiService);

    private loadingSubjects: Record<LoadingKey, BehaviorSubject<boolean>> = {
        fuelStation: new BehaviorSubject<boolean>(false),
        managers: new BehaviorSubject<boolean>(false),
        fuelOrders: new BehaviorSubject<boolean>(false),
        assignManager: new BehaviorSubject<boolean>(false),
        unassignManager: new BehaviorSubject<boolean>(false),
        confirmOrder: new BehaviorSubject<boolean>(false),
        rejectOrder: new BehaviorSubject<boolean>(false),
        changeFuelPrice: new BehaviorSubject<boolean>(false),
        deactivateFuelStation: new BehaviorSubject<boolean>(false),
    };

    loading = {
        fuelStation: this.loadingSubjects.fuelStation.asObservable(),
        managers: this.loadingSubjects.managers.asObservable(),
        fuelOrders: this.loadingSubjects.fuelOrders.asObservable(),
        assignManager: this.loadingSubjects.assignManager.asObservable(),
        unassignManager: this.loadingSubjects.unassignManager.asObservable(),
        confirmOrder: this.loadingSubjects.confirmOrder.asObservable(),
        rejectOrder: this.loadingSubjects.rejectOrder.asObservable(),
        changeFuelPrice: this.loadingSubjects.changeFuelPrice.asObservable(),
        deactivateFuelStation: this.loadingSubjects.deactivateFuelStation.asObservable(),
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

    getContextValue(): FuelStationContext | null {
        return this.contextSubject.value;
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
                tap(fuelOrders => this.updateContext({ fuelOrders })),
                catchError(error => {
                    console.error("Error fetching orders:", error);
                    return throwError(() => new Error("Failed to fetch fuel orders"));
                })
            )
        );
    }

    assignManager(managerId: number): Observable<Manager[]> {
        const { fuelStation } = this.contextValue;

        return this.withLoading(
            "assignManager",
            this.fuelStationApi.assignManager(fuelStation.id, managerId).pipe(
                switchMap(() => {
                    this.updateContext({ managers: [] })
                    return this.getAssignedManagers()
                }),
                catchError(error => {
                    console.error("Error assigning manager:", error);
                    return throwError(() => new Error(`Failed to assign manager with ID ${managerId}`));
                })
            )
        );
    }

    unassignManager(managerId: number): Observable<Manager[]> {
        const { fuelStation } = this.contextValue;

        return this.withLoading(
            "unassignManager",
            this.fuelStationApi.unassignManager(fuelStation.id, managerId).pipe(
                switchMap(() => {
                    this.updateContext({ managers: [] })
                    return this.getAssignedManagers()
                }),
                catchError(error => {
                    console.error("Error unassigning manager:", error);
                    return throwError(() => new Error(`Failed to unassign manager with ID ${managerId}`));
                })
            )
        );
    }

    confirmFuelOrder(fuelOrderId: number): Observable<FuelOrder[]> {
        // eslint-disable-next-line @typescript-eslint/no-unused-expressions
        this.contextValue;
        return this.withLoading(
            "confirmOrder",
            this.fuelOrderApi.confirmFuelOrder(fuelOrderId).pipe(
                switchMap(() => {
                    this.updateContext({ fuelOrders: [] })
                    return this.getFuelOrders();
                }),
                catchError(error => {
                    console.error("Error confirming fuel order:", error);
                    return throwError(() => new Error(`Failed to confirm fuel order with ID ${fuelOrderId}`));
                })
            )
        );
    }

    rejectFuelOrder(fuelOrderId: number): Observable<FuelOrder[]> {
        // eslint-disable-next-line @typescript-eslint/no-unused-expressions
        this.contextValue;
        return this.withLoading(
            "rejectOrder",
            this.fuelOrderApi.rejectFuelOrder(fuelOrderId).pipe(
                switchMap(() => {
                    this.updateContext({ fuelOrders: [] })
                    return this.getFuelOrders()
                }),
                catchError(error => {
                    console.error("Error rejecting fuel order:", error);
                    return throwError(() => new Error(`Failed to reject fuel order with ID ${fuelOrderId}`));
                })
            )
        );
    }

    changeFuelPrice(fuelGrade: FuelGrade, newPrice: number): Observable<FuelStation> {
        const { fuelStation, managers, fuelOrders } = this.contextValue;

        return this.withLoading(
            "changeFuelPrice",
            this.fuelStationApi.changeFuelPrice(fuelStation.id, fuelGrade, newPrice).pipe(
                tap(updatedStation => {
                    this.contextSubject.next(new FuelStationContext(updatedStation, managers, fuelOrders));
                }),
                catchError(error => {
                    console.error("Error changing fuel price:", error);
                    return throwError(() => new Error(`Failed to change fuel price for grade ${fuelGrade}`));
                })
            )
        );
    }

    deactivateFuelStation(): Observable<FuelStation> {
        const { fuelStation, managers, fuelOrders } = this.contextValue;

        return this.withLoading(
            "deactivateFuelStation",
            this.fuelStationApi.deactivateFuelStation(fuelStation.id).pipe(
                tap(updatedStation => {
                    this.contextSubject.next(new FuelStationContext(updatedStation, managers, fuelOrders));
                }),
                catchError(error => {
                    console.error("Error deactivating fuel station:", error);
                    return throwError(() => new Error("Failed to deactivate fuel station"));
                })
            )
        );
    }

    resetContext(): void {
        this.contextSubject.next(null);
    }
}
