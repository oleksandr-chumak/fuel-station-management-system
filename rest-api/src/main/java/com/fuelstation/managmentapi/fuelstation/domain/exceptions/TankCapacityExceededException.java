package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;

/**
 * Exception thrown when a refill would exceed tank capacity
 */
@Getter
public class TankCapacityExceededException extends FuelStationDomainException {
    private final long fuelTankId;
    private final float requestedVolume;
    private final float maxCapacity;

    public TankCapacityExceededException(long fuelTankId, float requestedVolume, float maxCapacity) {
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
