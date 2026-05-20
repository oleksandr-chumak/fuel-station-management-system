package com.fuelstation.managmentapi.fuelstation.application.rest.response;

import java.math.BigDecimal;
import java.time.Instant;

public record FuelPriceHistoryResponse(
        String fuelGrade,
        BigDecimal pricePerLiter,
        String currency,
        Instant changedAt,
        Long changedBy
) {}
