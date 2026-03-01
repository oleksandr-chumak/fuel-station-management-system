package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;

import java.time.Instant;

public final class FuelOrderCreated extends FuelOrderEvent {

    public FuelOrderCreated(long fuelOrderId, long fuelStationId, Actor performedBy) {
        super(FuelOrderEventType.FUEL_ORDER_CREATED, fuelOrderId, fuelStationId, performedBy);
    }

    public FuelOrderCreated(long fuelOrderId, long fuelStationId, Actor performedBy, Instant occurredAt) {
        super(FuelOrderEventType.FUEL_ORDER_CREATED, fuelOrderId, fuelStationId, performedBy, occurredAt);
    }

}
