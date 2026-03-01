package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.Getter;

import java.time.Instant;

@Getter
public class FuelStationDeactivated extends FuelStationEvent {

    public FuelStationDeactivated(long fuelStationId, Actor performedBy) {
        super(FuelStationEventType.FUEL_STATION_DEACTIVATED, fuelStationId, performedBy);
    }

    public FuelStationDeactivated(long fuelStationId, Actor performedBy, Instant occurredAt) {
        super(FuelStationEventType.FUEL_STATION_DEACTIVATED, fuelStationId, performedBy, occurredAt);
    }

}
