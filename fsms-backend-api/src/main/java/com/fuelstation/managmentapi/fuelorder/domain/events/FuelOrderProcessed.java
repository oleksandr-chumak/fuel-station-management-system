package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

public record FuelOrderProcessed(
        FuelOrderEventType type,
        long fuelOrderId,
        long fuelStationId
) implements DomainEvent {

    public FuelOrderProcessed(long fuelOrderId, long fuelStationId) {
        this(FuelOrderEventType.FUEL_ORDER_PROCESSED, fuelOrderId, fuelStationId);
    }

}
