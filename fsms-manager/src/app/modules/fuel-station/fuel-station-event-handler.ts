import { inject, Injectable } from "@angular/core";
import {
    FuelOrder,
    FuelOrderConfirmed,
    FuelOrderCreated,
    FuelOrderEvent,
    FuelOrderProcessed,
    FuelOrderRejected,
    FuelOrderRestClient,
    FuelPriceChanged,
    FuelStationEvent,
    FuelStationRestClient,
    FuelStationStompClient,
    ManagerAssignedToFuelStation,
    ManagerRestClient,
    ManagerUnassignedFromFuelStation,
} from "fsms-web-api";
import { catchError, EMPTY, Observable, tap } from "rxjs";
import { LoggerService } from "../common/logger";
import { FuelStationStore } from "./fuel-station-store";
import { MessageService } from "primeng/api";
import { Router } from "@angular/router";
import { AuthService } from "fsms-security";

@Injectable({ providedIn: "root" })
export class FuelStationEventHandler {

    private readonly messageService = inject(MessageService);
    private readonly logger = inject(LoggerService);
    private readonly router = inject(Router);

    private readonly authService = inject(AuthService);
    private readonly fuelStationStore = inject(FuelStationStore);
    private readonly fuelOrderRestClient = inject(FuelOrderRestClient);
    private readonly fuelStationStompClient = inject(FuelStationStompClient);
    private readonly fuelStationRestClient = inject(FuelStationRestClient);

    start(fuelStationId: number): Observable<FuelStationEvent | FuelOrderEvent> {
        this.logger.log('[FuelStationEventHandler] Starting event subscription for fuelStationId:', fuelStationId);
        return this.fuelStationStompClient.onFuelStationAll(fuelStationId).pipe(
            tap((event) => {
                this.logger.log('[FuelStationEventHandler] Event received:', event.constructor.name, event);
                if (event instanceof FuelPriceChanged) {
                    this.handleFuelPriceChanged(event);
                } else if (event instanceof ManagerAssignedToFuelStation) {
                    this.handleManagerAssigned(event);
                } else if (event instanceof ManagerUnassignedFromFuelStation) {
                    this.handleManagerUnassigned(event);
                } else if (event instanceof FuelOrderCreated) {
                    this.handleFuelOrderCreated(event);
                } else if (event instanceof FuelOrderConfirmed) {
                    this.handleFuelOrderConfirmed(event);
                } else if (event instanceof FuelOrderRejected) {
                    this.handleFuelOrderRejected(event);
                } else if (event instanceof FuelOrderProcessed) {
                    this.handleFuelOrderProcessed(event);
                }
            })
        );
    }

    private handleFuelPriceChanged(event: FuelPriceChanged): void {
        const newFuelStation = this.fuelStationStore.fuelStation.clone();
        newFuelStation.updateFuelPrice(event.fuelGrade, event.pricePerLiter);
        this.fuelStationStore.fuelStation = newFuelStation;
    }

    private handleManagerAssigned(event: ManagerAssignedToFuelStation): void {
        if (this.fuelStationStore.fuelStation.isManagerAssigned(event.managerId)) {
            return;
        }

        const newFuelStation = this.fuelStationStore.fuelStation.clone();
        newFuelStation.assignManger(event.managerId);
        this.fuelStationStore.fuelStation = newFuelStation;

        this.fuelStationRestClient.getAssignedManagers(event.fuelStationId)
            .pipe(
                tap((managers) => this.fuelStationStore.managers = managers),
                catchError((e) => {
                    this.logger.error('[FuelStationEventHandler] Error fetching manager:', e);
                    return EMPTY;
                })
            )
            .subscribe();
    }

    private async handleManagerUnassigned(event: ManagerUnassignedFromFuelStation): Promise<void> {
        if (event.managerId === this.authService.getUserValue()?.userId) {
            await this.router.navigate(["/"]);
            this.fuelStationStore.reset();
            this.messageService.add({
                severity: "info",
                summary: "Unassigned from Fuel Station",
                detail: "You have been unassigned from this fuel station. You have been redirected to the home page."
            });
            return;
        }

        const newFuelStation = this.fuelStationStore.fuelStation.clone();
        newFuelStation.unassignManger(event.managerId);
        this.fuelStationStore.fuelStation = newFuelStation;

        this.fuelStationStore.managers = this.fuelStationStore.managers.filter(
            (manager) => manager.credentialsId !== event.managerId
        );
    }

    private handleFuelOrderCreated(event: FuelOrderCreated): void {
        this.fuelOrderRestClient.getFuelOrderById(event.fuelOrderId)
            .pipe(
                tap((fuelOrder) => this.fuelStationStore.fuelOrders = [fuelOrder, ...this.fuelStationStore.fuelOrders])
            )
            .subscribe();
    }

    private handleFuelOrderConfirmed(event: FuelOrderConfirmed): void {
        this.updateFuelOrder(event.fuelOrderId, (order) => order.confirm());
    }

    private handleFuelOrderRejected(event: FuelOrderRejected): void {
        this.updateFuelOrder(event.fuelOrderId, (order) => order.reject());
    }

    private handleFuelOrderProcessed(event: FuelOrderProcessed): void {
        this.fuelStationRestClient.getFuelStationById(event.fuelStationId)
            .pipe(tap((fuelStation) => this.fuelStationStore.fuelStation = fuelStation))
            .subscribe();

        this.updateFuelOrder(event.fuelOrderId, (order) => order.process());
    }

    private updateFuelOrder(fuelOrderId: number, cb: (order: FuelOrder) => void): void {
        this.fuelStationStore.fuelOrders = this.fuelStationStore.fuelOrders.map((fuelOrder) => {
            if (fuelOrder.fuelOrderId === fuelOrderId) {
                cb(fuelOrder);
            }
            return fuelOrder;
        });
    }
}
