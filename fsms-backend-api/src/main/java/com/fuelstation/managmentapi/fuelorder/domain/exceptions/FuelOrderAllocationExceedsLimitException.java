package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class FuelOrderAllocationExceedsLimitException extends FuelOrderDomainException {

    public FuelOrderAllocationExceedsLimitException(
        long fuelTankId,
        BigDecimal requestedVolume,
        BigDecimal tankAvailableVolume,
        BigDecimal pendingVolume,
        BigDecimal allowedVolume,
        FuelGrade fuelGrade,
        long fuelStationId
    ) {
        super(
            String.format(
                "Allocation of %sL for fuel tank %d exceeds the allowed limit. " +
                    "Tank available capacity: %sL, reserved by pending orders: %sL, " +
                    "remaining allowed: %sL (fuel grade: %s, station: %d).",
                requestedVolume,
                fuelTankId,
                tankAvailableVolume,
                pendingVolume,
                allowedVolume,
                fuelGrade,
                fuelStationId
            ),
            "ALLOCATION_EXCEEDS_LIMIT",
            buildDetails(fuelTankId, requestedVolume, tankAvailableVolume, pendingVolume, allowedVolume, fuelGrade, fuelStationId)
        );
    }

    private static Map<String, Object> buildDetails(
        long fuelTankId,
        BigDecimal requestedVolume,
        BigDecimal tankAvailableVolume,
        BigDecimal pendingVolume,
        BigDecimal allowedVolume,
        FuelGrade fuelGrade,
        long fuelStationId
    ) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("fuelTankId", fuelTankId);
        details.put("requestedVolume", requestedVolume);
        details.put("tankAvailableVolume", tankAvailableVolume);
        details.put("pendingVolume", pendingVolume);
        details.put("allowedVolume", allowedVolume);
        details.put("fuelGrade", fuelGrade.toString());
        details.put("fuelStationId", fuelStationId);
        return details;
    }
}
