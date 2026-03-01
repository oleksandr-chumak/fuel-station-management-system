package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.Getter;

import java.time.Instant;

@Getter
public class FuelStationCreated extends FuelStationEvent {
    private final FuelStationEventType type = FuelStationEventType.FUEL_STATION_CREATED;

    public FuelStationCreated(long fuelStationId, Actor performedBy) {
        super(FuelStationEventType.FUEL_STATION_CREATED, fuelStationId, performedBy);
    }

    public FuelStationCreated(long fuelStationId, Actor performedBy, Instant occurredAt) {
        super(FuelStationEventType.FUEL_STATION_CREATED, fuelStationId, performedBy, occurredAt);
    }
}
