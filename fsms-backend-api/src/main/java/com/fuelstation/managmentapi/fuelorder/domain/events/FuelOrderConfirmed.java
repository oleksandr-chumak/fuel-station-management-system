package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

public record FuelOrderConfirmed(
        FuelOrderEventType type,
        long fuelOrderId,
        long fuelStationId
) implements DomainEvent {
    public FuelOrderConfirmed(long fuelOrderId, long fuelStationId) {
        this(FuelOrderEventType.FUEL_ORDER_CONFIRMED, fuelOrderId, fuelStationId);
    }
}
