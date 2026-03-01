package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;

import java.time.Instant;

public final class FuelOrderProcessed extends FuelOrderEvent {

    public FuelOrderProcessed(long fuelOrderId, long fuelStationId, Actor performedBy) {
        super(FuelOrderEventType.FUEL_ORDER_PROCESSED, fuelOrderId, fuelStationId, performedBy);
    }

    public FuelOrderProcessed(long fuelOrderId, long fuelStationId, Actor performedBy, Instant occurredAt) {
        super(FuelOrderEventType.FUEL_ORDER_PROCESSED, fuelOrderId, fuelStationId, performedBy, occurredAt);
    }

}
