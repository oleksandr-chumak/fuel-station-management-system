package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;

@Getter
public class FuelTankAlreadyDecommissionedException extends FuelStationDomainException {
    private final long fuelStationId;
    private final long fuelTankId;

    public FuelTankAlreadyDecommissionedException(long fuelStationId, long fuelTankId) {
        super(
            String.format("Fuel tank %d in fuel station %d is already decommissioned", fuelTankId, fuelStationId),
            "TANK_ALREADY_DECOMMISSIONED"
        );
        this.fuelStationId = fuelStationId;
        this.fuelTankId = fuelTankId;
    }
}
