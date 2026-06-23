package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FuelTankNotFoundException extends FuelStationDomainException {
    private final long fuelStationId;
    private final long fuelTankId;

    public FuelTankNotFoundException(long fuelStationId, long fuelTankId) {
        super(
            String.format("Fuel tank %d not found in fuel station %d", fuelTankId, fuelStationId),
            HttpStatus.NOT_FOUND,
            "TANK_NOT_FOUND"
        );
        this.fuelStationId = fuelStationId;
        this.fuelTankId = fuelTankId;
    }
}
