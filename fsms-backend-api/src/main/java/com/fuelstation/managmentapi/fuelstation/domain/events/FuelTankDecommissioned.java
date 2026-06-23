package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.Getter;

import java.time.Instant;

@Getter
public class FuelTankDecommissioned extends FuelStationEvent {
    private final long fuelTankId;

    public FuelTankDecommissioned(long fuelStationId, long fuelTankId, Actor performedBy) {
        super(FuelStationEventType.FUEL_TANK_DECOMMISSIONED, fuelStationId, performedBy);
        this.fuelTankId = fuelTankId;
    }

    public FuelTankDecommissioned(long fuelStationId, long fuelTankId, Actor performedBy, Instant occurredAt) {
        super(FuelStationEventType.FUEL_TANK_DECOMMISSIONED, fuelStationId, performedBy, occurredAt);
        this.fuelTankId = fuelTankId;
    }
}
