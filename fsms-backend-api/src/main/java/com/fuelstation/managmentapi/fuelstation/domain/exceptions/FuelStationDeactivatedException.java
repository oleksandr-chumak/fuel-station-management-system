package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

public class FuelStationDeactivatedException extends FuelStationDomainException {

    public FuelStationDeactivatedException(long fuelStationId) {
        var message = String.format(
                "Fuel station with id: %d has been deactivated. No operations can be performed.",
                fuelStationId
        );
        super(message, "DEACTIVATED");
    }

}
