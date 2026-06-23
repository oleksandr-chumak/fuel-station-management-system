package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InsufficientFuelVolumeException extends FuelStationDomainException {
    private final long fuelTankId;
    private final BigDecimal requestedVolume;
    private final BigDecimal availableVolume;

    public InsufficientFuelVolumeException(long fuelTankId, BigDecimal requestedVolume, BigDecimal availableVolume) {
        super(
            String.format("Dispense request of %.2f liters exceeds available volume of %.2f liters in tank %d",
                requestedVolume, availableVolume, fuelTankId),
            "INSUFFICIENT_FUEL_VOLUME"
        );
        this.fuelTankId = fuelTankId;
        this.requestedVolume = requestedVolume;
        this.availableVolume = availableVolume;
    }
}
