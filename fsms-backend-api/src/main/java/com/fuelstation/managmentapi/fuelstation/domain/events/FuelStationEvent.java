package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEvent;
import lombok.Getter;

import java.time.Instant;

@Getter
public class FuelStationEvent extends DomainEvent {
    private final FuelStationEventType type;
    private final long fuelStationId;

    public FuelStationEvent(FuelStationEventType type, long fuelStationId, Actor performedBy) {
        super(performedBy);
        this.type = type;
        this.fuelStationId = fuelStationId;
    }

    public FuelStationEvent(FuelStationEventType type, long fuelStationId, Actor performedBy, Instant occurredAt) {
        super(performedBy, occurredAt);
        this.type = type;
        this.fuelStationId = fuelStationId;
    }
}
