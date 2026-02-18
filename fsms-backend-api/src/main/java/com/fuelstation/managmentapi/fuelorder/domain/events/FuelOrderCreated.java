package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

public record FuelOrderCreated(
        FuelOrderEventType type,
        long fuelOrderId,
        long fuelStationId
) implements DomainEvent {

    public FuelOrderCreated(long fuelOrderId, long fuelStationId) {
        this(FuelOrderEventType.FUEL_ORDER_CREATED, fuelOrderId, fuelStationId);
    }

}
