package com.fuelstation.managmentapi.fuelstation.domain.events;

import com.fuelstation.managmentapi.common.domain.DomainEvent;

import lombok.Getter;

@Getter
public class ManagerAssignedToFuelStation implements DomainEvent{
    private final FuelStationEventType type = FuelStationEventType.MANAGER_ASSIGNED_TO_FUEL_STATION;
    private final long fuelStationId;
    private final long managerId;

    public ManagerAssignedToFuelStation(long fuelStationId, long managerId) {
        this.fuelStationId = fuelStationId;
        this.managerId = managerId;
    }
}
