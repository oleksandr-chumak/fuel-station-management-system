package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;

public final class FuelOrderRejected extends FuelOrderEvent {

    public FuelOrderRejected(long fuelOrderId, long fuelStationId, Actor performedBy) {
        super(FuelOrderEventType.FUEL_ORDER_REJECTED, fuelOrderId, fuelStationId, performedBy);
    }

}
