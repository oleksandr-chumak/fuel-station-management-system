package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FuelTankNotEmptyException extends FuelStationDomainException {
    private final long fuelStationId;
    private final long fuelTankId;
    private final BigDecimal currentVolume;

    public FuelTankNotEmptyException(long fuelStationId, long fuelTankId, BigDecimal currentVolume) {
        super(
            String.format(
                "Fuel tank %d in fuel station %d cannot be decommissioned while it still holds %.2f liters of fuel",
                fuelTankId, fuelStationId, currentVolume
            ),
            "TANK_NOT_EMPTY"
        );
        this.fuelStationId = fuelStationId;
        this.fuelTankId = fuelTankId;
        this.currentVolume = currentVolume;
    }
}
