package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * Exception thrown when a refill would exceed tank capacity
 */
@Getter
public class TankCapacityExceededException extends FuelStationDomainException {
    private final long fuelTankId;
    private final BigDecimal requestedVolume;
    private final BigDecimal maxCapacity;

    public TankCapacityExceededException(long fuelTankId, BigDecimal requestedVolume, BigDecimal maxCapacity) {
        super(
            String.format("Refill request of %.2f liters exceeds tank %d capacity of %.2f liters", 
                requestedVolume, fuelTankId, maxCapacity),
            "TANK_CAPACITY_EXCEEDED"
        );
        this.fuelTankId = fuelTankId;
        this.requestedVolume = requestedVolume;
        this.maxCapacity = maxCapacity;
    }
}
