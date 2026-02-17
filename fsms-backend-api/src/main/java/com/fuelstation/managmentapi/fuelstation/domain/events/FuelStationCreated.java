package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class FuelStationCreated implements DomainEvent {
    private final FuelStationEventType type = FuelStationEventType.FUEL_STATION_CREATED;
    private final long fuelStationId;

    public FuelStationCreated(long fuelStationId) {
        this.fuelStationId = fuelStationId;
    }
}
