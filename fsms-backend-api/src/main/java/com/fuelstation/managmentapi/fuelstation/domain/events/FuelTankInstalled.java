package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class FuelTankInstalled extends FuelStationEvent {
    private final long fuelTankId;
    private final FuelGrade fuelGrade;
    private final BigDecimal maxCapacity;

    public FuelTankInstalled(long fuelStationId, long fuelTankId, FuelGrade fuelGrade, BigDecimal maxCapacity, Actor performedBy) {
        super(FuelStationEventType.FUEL_TANK_INSTALLED, fuelStationId, performedBy);
        this.fuelTankId = fuelTankId;
        this.fuelGrade = fuelGrade;
        this.maxCapacity = maxCapacity;
    }

    public FuelTankInstalled(long fuelStationId, long fuelTankId, FuelGrade fuelGrade, BigDecimal maxCapacity, Actor performedBy, Instant occurredAt) {
        super(FuelStationEventType.FUEL_TANK_INSTALLED, fuelStationId, performedBy, occurredAt);
        this.fuelTankId = fuelTankId;
        this.fuelGrade = fuelGrade;
        this.maxCapacity = maxCapacity;
    }
}
