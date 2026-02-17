package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class ManagerUnassignedFromFuelStation implements DomainEvent {
    private final FuelStationEventType type = FuelStationEventType.MANAGER_UNASSIGNED_FROM_FUEL_STATION;
    private final long fuelStationId;
    private final long managerId;

    public ManagerUnassignedFromFuelStation(long fuelStationId, long managerId) {
        this.fuelStationId = fuelStationId;
        this.managerId = managerId;
    }
}
