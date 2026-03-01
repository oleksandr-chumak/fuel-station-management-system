package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;

public final class FuelOrderCreated extends FuelOrderEvent {

    public FuelOrderCreated(long fuelOrderId, long fuelStationId, Actor performedBy) {
        super(FuelOrderEventType.FUEL_ORDER_CREATED, fuelOrderId, fuelStationId, performedBy);
    }

}
