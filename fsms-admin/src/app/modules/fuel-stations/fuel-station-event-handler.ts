import { inject, Injectable } from "@angular/core";
import {
  FuelOrderConfirmed,
  FuelOrderCreated,
  FuelOrderEvent,
  FuelOrderProcessed,
  FuelOrderRejected,
  FuelOrderRestClient,
  FuelPriceChanged,
  FuelStationDeactivated,
  FuelStationEvent,
  FuelStationRestClient,
  FuelStationStompClient,
  ManagerAssignedToFuelStation,
  ManagerRestClient,
  ManagerUnassignedFromFuelStation,
} from "fsms-web-api";
import { Observable, tap } from "rxjs";
import { FuelStationStore } from "./fuel-station-store";
import { LoggerService } from "../common/logger";

@Injectable({ providedIn: "root" })
export class FuelStationEventHandler {
  private readonly store = inject(FuelStationStore);
  private readonly managerRestClient = inject(ManagerRestClient);
  private readonly fuelOrderRestClient = inject(FuelOrderRestClient);
  private readonly fuelStationRestClient = inject(FuelStationRestClient);
  private readonly fuelStationStompClient = inject(FuelStationStompClient);
  private readonly logger = inject(LoggerService);

  start(fuelStationId: number): Observable<FuelStationEvent | FuelOrderEvent> {
    this.logger.log('[FuelStationEventHandler] Starting event subscription for fuelStationId:', fuelStationId);
    return this.fuelStationStompClient.onAll(fuelStationId).pipe(
      tap((event) => {
        this.logger.log('[FuelStationEventHandler] Event received:',event.constructor.name, event);
        if (event instanceof FuelPriceChanged) {
          this.handleFuelPriceChanged(event);
        } else if (event instanceof FuelStationDeactivated) {
          this.handleFuelStationDeactivated(event);
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
    const newFuelStation = this.store.fuelStation.clone();
    newFuelStation.updateFuelPrice(event.fuelGrade, event.pricePerLiter);
    this.store.fuelStation = newFuelStation;
  }

  private handleFuelStationDeactivated(event: FuelStationDeactivated): void {
    // TODO
  }

  private handleManagerAssigned(event: ManagerAssignedToFuelStation): void {
    if(this.store.fuelStation.isManagerAssigned(event.managerId)) {
      return;
    }

    const newFuelStation = this.store.fuelStation.clone();
    newFuelStation.assignManger(event.managerId);
    this.store.fuelStation = newFuelStation;

    this.managerRestClient.getManagerById(event.managerId)
      .pipe(tap((manager) => {
        this.store.managers = [...this.store.managers, manager];
      }))
      .subscribe();
  }

  private handleManagerUnassigned(event: ManagerUnassignedFromFuelStation): void {
    const newFuelStation = this.store.fuelStation.clone();
    newFuelStation.unassignManger(event.managerId);
    this.store.managers = this.store.managers.filter(
      (manager) => manager.credentialsId !== event.managerId
    );
    this.store.fuelStation = newFuelStation;
  }

  private handleFuelOrderCreated(event: FuelOrderCreated): void {
    this.fuelOrderRestClient.getFuelOrderById(event.fuelOrderId)
      .pipe(tap((fuelOrder) => {
        this.store.fuelOrders = [...this.store.fuelOrders, fuelOrder];
      }))
      .subscribe();
  }

  private handleFuelOrderConfirmed(event: FuelOrderConfirmed): void {
    this.updateFuelOrder(event.fuelOrderId, (order) => order.confirm());
  }

  private handleFuelOrderRejected(event: FuelOrderRejected): void {
    this.updateFuelOrder(event.fuelOrderId, (order) => order.reject());
  }

  private handleFuelOrderProcessed(event: FuelOrderProcessed): void {
    this.updateFuelOrder(event.fuelOrderId, (order) => order.process());

    this.fuelStationRestClient.getFuelStationById(event.fuelStationId)
      .pipe(tap((fuelStation) => this.store.fuelStation = fuelStation))
      .subscribe()
  }

  private updateFuelOrder(fuelOrderId: number, action: (order: any) => void): void {
    this.store.fuelOrders = this.store.fuelOrders.map((fuelOrder) => {
      if (fuelOrder.fuelOrderId === fuelOrderId) {
        action(fuelOrder);
      }
      return fuelOrder;
    });
  }
}