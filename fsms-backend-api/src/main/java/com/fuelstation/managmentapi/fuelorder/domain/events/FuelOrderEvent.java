package com.fuelstation.managmentapi.fuelorder.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEvent;
import lombok.Getter;


@Getter
public class FuelOrderEvent extends DomainEvent {
    private final FuelOrderEventType type;
    private final long fuelOrderId;
    private final long fuelStationId;

    public FuelOrderEvent(
            FuelOrderEventType type,
            long fuelOrderId,
            long fuelStationId,
            Actor performedBy
    ) {
        super(performedBy);
        this.type = type;
        this.fuelOrderId = fuelOrderId;
        this.fuelStationId = fuelStationId;
    }

    public FuelOrderEvent(
            FuelOrderEventType type,
            long fuelOrderId,
            long fuelStationId,
            Actor performedBy,
            java.time.Instant occurredAt
    ) {
        super(performedBy, occurredAt);
        this.type = type;
        this.fuelOrderId = fuelOrderId;
        this.fuelStationId = fuelStationId;
    }
}
