package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;

import java.time.Instant;

public final class FuelOrderRejected extends FuelOrderEvent {

    public FuelOrderRejected(long fuelOrderId, long fuelStationId, Actor performedBy) {
        super(FuelOrderEventType.FUEL_ORDER_REJECTED, fuelOrderId, fuelStationId, performedBy);
    }

    public FuelOrderRejected(long fuelOrderId, long fuelStationId, Actor performedBy, Instant occurredAt) {
        super(FuelOrderEventType.FUEL_ORDER_REJECTED, fuelOrderId, fuelStationId, performedBy, occurredAt);
    }

}
