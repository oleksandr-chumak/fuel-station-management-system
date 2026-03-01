package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ManagerUnassignedFromFuelStation extends FuelStationEvent {
    private final long managerId;

    public ManagerUnassignedFromFuelStation(long fuelStationId, long managerId, Actor performedBy) {
        super(FuelStationEventType.MANAGER_UNASSIGNED_FROM_FUEL_STATION, fuelStationId, performedBy);
        this.managerId = managerId;
    }

    public ManagerUnassignedFromFuelStation(long fuelStationId, long managerId, Actor performedBy, Instant occurredAt) {
        super(FuelStationEventType.MANAGER_UNASSIGNED_FROM_FUEL_STATION, fuelStationId, performedBy, occurredAt);
        this.managerId = managerId;
    }
}
