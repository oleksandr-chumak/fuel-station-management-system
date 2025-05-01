package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;

@Getter
public class FuelStationNotFoundException extends FuelStationDomainException {

    private final long fuelStationId;

    public FuelStationNotFoundException(long fuelStationId) {
        super(
            String.format("Fuel station %d not found", fuelStationId),
            "NOT_FOUND"
        );
        this.fuelStationId = fuelStationId;
    }
    
}
