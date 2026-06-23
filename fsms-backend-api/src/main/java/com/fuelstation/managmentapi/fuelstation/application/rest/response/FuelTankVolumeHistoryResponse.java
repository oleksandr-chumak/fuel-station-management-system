package com.fuelstation.managmentapi.fuelstation.application.rest.response;

import java.math.BigDecimal;
import java.time.Instant;

public record FuelTankVolumeHistoryResponse(
        long fuelTankId,
        String fuelGrade,
        BigDecimal oldVolume,
        BigDecimal newVolume,
        String reason,
        Instant changedAt,
        Long changedBy
) {}
