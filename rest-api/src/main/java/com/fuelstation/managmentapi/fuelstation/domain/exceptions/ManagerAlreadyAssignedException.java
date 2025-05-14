package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;

/**
 * Exception thrown when attempting to assign a manager who is already assigned
 */
@Getter
public class ManagerAlreadyAssignedException extends FuelStationDomainException {
    private final long managerId;
    private final long fuelStationId;

    public ManagerAlreadyAssignedException(long managerId, long fuelStationId) {
        super(
            String.format("Manager %d is already assigned to fuel station %d", managerId, fuelStationId),
            "MANAGER_ALREADY_ASSIGNED"
        );
        this.managerId = managerId;
        this.fuelStationId = fuelStationId;
    }
}
