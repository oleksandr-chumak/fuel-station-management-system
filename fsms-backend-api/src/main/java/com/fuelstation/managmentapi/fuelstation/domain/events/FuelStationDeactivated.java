package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationDomainException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class FuelStationDeactivated implements DomainEvent {
    private final FuelStationEventType type = FuelStationEventType.FUEL_STATION_DEACTIVATED;
    private final long fuelStationId;

    public FuelStationDeactivated(long fuelStationId) {
        this.fuelStationId = fuelStationId;
    }
}
