package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;

/**
 * Exception thrown when attempting to deactivate an already deactivated fuel station
 */
@Getter
public class FuelStationAlreadyDeactivatedException extends FuelStationDomainException {
    private final long fuelStationId;

    public FuelStationAlreadyDeactivatedException(long fuelStationId) {
        super(
            String.format("Fuel station %d is already deactivated", fuelStationId),
            "ALREADY_DEACTIVATED"
        );
        this.fuelStationId = fuelStationId;
    }
}