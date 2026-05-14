package com.fuelstation.managmentapi.fuelpurchase.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public final class FuelPurchaseCreated extends DomainEvent {

    private final FuelPurchaseEventType type = FuelPurchaseEventType.FUEL_PURCHASE_CREATED;
    private final Long fuelPurchaseId;
    private final Long fuelStationId;
    private final Long fuelOrderId;

    public FuelPurchaseCreated(Long fuelPurchaseId, Long fuelStationId, Long fuelOrderId, Actor performedBy) {
        super(performedBy);
        this.fuelPurchaseId = fuelPurchaseId;
        this.fuelStationId = fuelStationId;
        this.fuelOrderId = fuelOrderId;
    }
}
