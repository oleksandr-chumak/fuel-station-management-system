package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import java.math.BigDecimal;

public class FuelOrderAmountExceedsLimitException extends FuelOrderDomainException {

    public FuelOrderAmountExceedsLimitException(
        BigDecimal amount,
        BigDecimal availableVolume,
        BigDecimal pendingAmount,
        BigDecimal allowedAmount,
        FuelGrade fuelGrade,
        long fuelStationId
    ) {
        super(
            "Ordered amount (" + amount + "L) exceeds available tank space (" + availableVolume + "L) minus pending orders (" +
            pendingAmount + "L) for " + fuelGrade.toString() + " at station ID " + fuelStationId + ". Max allowed: " + allowedAmount + "L.",
            "AMOUNT_EXCEEDS_LIMIT"
        );
    }
}
