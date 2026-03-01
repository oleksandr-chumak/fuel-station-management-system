package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.Getter;

import java.time.Instant;

@Getter
public final class FuelOrderConfirmed extends FuelOrderEvent {

    public FuelOrderConfirmed(long fuelOrderId, long fuelStationId, Actor performedBy) {
        super(FuelOrderEventType.FUEL_ORDER_CONFIRMED, fuelOrderId, fuelStationId, performedBy);
    }

    public FuelOrderConfirmed(long fuelOrderId, long fuelStationId, Actor performedBy, Instant occurredAt) {
        super(FuelOrderEventType.FUEL_ORDER_CONFIRMED, fuelOrderId, fuelStationId, performedBy, occurredAt);
    }

}
