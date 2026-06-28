package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import org.springframework.http.HttpStatus;

public class FuelOrderTankNotOnStationException extends FuelOrderDomainException {
    public FuelOrderTankNotOnStationException(long fuelTankId, long fuelStationId) {
        super(
            String.format(
                "Fuel tank %d does not belong to station %d.",
                fuelTankId, fuelStationId
            ),
            HttpStatus.BAD_REQUEST,
            "TANK_NOT_ON_STATION"
        );
    }
}
