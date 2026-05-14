package com.fuelstation.managmentapi.fuelprice.domain;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.common.domain.FuelUnit;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record FuelPrice(
    Long fuelPriceId,
    FuelGrade fuelGrade,
    FuelUnit unit,
    BigDecimal price,
    CurrencyCode currency,
    String source,
    OffsetDateTime fetchedAt
) {
}
