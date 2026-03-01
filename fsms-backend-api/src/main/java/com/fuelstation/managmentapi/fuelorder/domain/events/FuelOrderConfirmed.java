package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.Getter;

@Getter
public final class FuelOrderConfirmed extends FuelOrderEvent {

    public FuelOrderConfirmed(long fuelOrderId, long fuelStationId, Actor performedBy) {
        super(FuelOrderEventType.FUEL_ORDER_CONFIRMED, fuelOrderId, fuelStationId, performedBy);
    }

}
