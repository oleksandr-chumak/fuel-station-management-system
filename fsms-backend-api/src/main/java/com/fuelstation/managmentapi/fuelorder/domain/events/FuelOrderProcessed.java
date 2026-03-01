package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;

public final class FuelOrderProcessed extends FuelOrderEvent {

    public FuelOrderProcessed(long fuelOrderId, long fuelStationId, Actor performedBy) {
        super(FuelOrderEventType.FUEL_ORDER_PROCESSED, fuelOrderId, fuelStationId, performedBy);
    }

}
