package com.fuelstation.managmentapi.fuelstation.application.rest;

import com.fuelstation.managmentapi.common.application.exceptions.NotFoundException;

import lombok.Getter;

@Getter
public class FuelStationNotFoundException extends NotFoundException {

    private final long fuelStationId;

    public FuelStationNotFoundException(long fuelStationId) {
        super(
            String.format("Fuel station %d not found", fuelStationId),
            "FUEL_STATION"
        );
        this.fuelStationId = fuelStationId;
    }
    
}
