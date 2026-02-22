import { inject, Injectable } from "@angular/core";
import {
  FuelOrderConfirmed,
  FuelOrderCreated,
  FuelOrderEvent,
  FuelOrderProcessed,
  FuelOrderRejected,
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
import { LoggerService } from "../common/logger";
import { FuelOrderEventHandler } from "../fuel-orders/fuel-order-event-handler";
import { FuelStationStore } from "./fuel-station-store";

@Injectable({ providedIn: "root" })
export class FuelStationEventHandler {

  private readonly fuelStationStore = inject(FuelStationStore);
  private readonly managerRestClient = inject(ManagerRestClient);
  private readonly fuelStationStompClient = inject(FuelStationStompClient);
  private readonly fuelStationRestClient = inject(FuelStationRestClient);
  private readonly fuelOrderEventHandler = inject(FuelOrderEventHandler);
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
    const newFuelStation = this.fuelStationStore.fuelStation.clone();
    newFuelStation.updateFuelPrice(event.fuelGrade, event.pricePerLiter);
    this.fuelStationStore.fuelStation = newFuelStation;
  }

  private handleFuelStationDeactivated(event: FuelStationDeactivated): void {
    // TODO
  }

  private handleManagerAssigned(event: ManagerAssignedToFuelStation): void {
    if(this.fuelStationStore.fuelStation.isManagerAssigned(event.managerId)) {
      return;
    }

    const newFuelStation = this.fuelStationStore.fuelStation.clone();
    newFuelStation.assignManger(event.managerId);
    this.fuelStationStore.fuelStation = newFuelStation;

    if(this.fuelStationStore.managers === null) {
      return;
    }

    this.managerRestClient.getManagerById(event.managerId)
      .pipe(tap((manager) => {
        if(this.fuelStationStore.managers === null) {
          return;
        }
        this.fuelStationStore.managers = [...this.fuelStationStore.managers, manager];
      }))
      .subscribe();
  }

  private handleManagerUnassigned(event: ManagerUnassignedFromFuelStation): void {
    const newFuelStation = this.fuelStationStore.fuelStation.clone();
    newFuelStation.unassignManger(event.managerId);

    this.fuelStationStore.fuelStation = newFuelStation;

    if(this.fuelStationStore.managers !== null) {
      this.fuelStationStore.managers = this.fuelStationStore.managers.filter(
        (manager) => manager.credentialsId !== event.managerId
      );
    }
  }

  private handleFuelOrderCreated(event: FuelOrderCreated): void {
    if(this.fuelStationStore.fuelOrders === null) {
      return;
    }

    this.fuelOrderEventHandler.handleFuelOrderCreated(event.fuelOrderId, this.fuelStationStore.fuelOrders)
      .pipe(tap((fuelOrders) => this.fuelStationStore.fuelOrders = fuelOrders))
      .subscribe()
  }

  private handleFuelOrderConfirmed(event: FuelOrderConfirmed): void {
    if(this.fuelStationStore.fuelOrders === null) {
      return;
    }

    this.fuelStationStore.fuelOrders = this.fuelOrderEventHandler
      .handleFuelOrderConfirmed(event.fuelOrderId, this.fuelStationStore.fuelOrders)
  }

  private handleFuelOrderRejected(event: FuelOrderRejected): void {
    if(this.fuelStationStore.fuelOrders === null) {
      return;
    }

    this.fuelStationStore.fuelOrders = this.fuelOrderEventHandler
      .handleFuelOrderRejected(event.fuelOrderId, this.fuelStationStore.fuelOrders)
  }

  private handleFuelOrderProcessed(event: FuelOrderProcessed): void {
    this.fuelStationRestClient.getFuelStationById(event.fuelStationId)
      .pipe(tap((fuelStation) => this.fuelStationStore.fuelStation = fuelStation))
      .subscribe()

    if(this.fuelStationStore.fuelOrders === null) {
      return;
    }
    this.fuelStationStore.fuelOrders = this.fuelOrderEventHandler
      .handleFuelOrderProcessed(event.fuelOrderId, this.fuelStationStore.fuelOrders)
  }

}