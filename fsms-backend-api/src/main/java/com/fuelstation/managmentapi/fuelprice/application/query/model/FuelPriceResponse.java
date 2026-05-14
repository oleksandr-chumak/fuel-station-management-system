package com.fuelstation.managmentapi.fuelprice.application.query.model;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.common.domain.FuelUnit;
import com.fuelstation.managmentapi.fuelprice.domain.FuelPrice;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record FuelPriceResponse(
    Long fuelPriceId,
    FuelGrade fuelGrade,
    FuelUnit unit,
    BigDecimal price,
    CurrencyCode currency,
    String source,
    OffsetDateTime fetchedAt
) {
    public static FuelPriceResponse from(FuelPrice domain) {
        return new FuelPriceResponse(
            domain.fuelPriceId(),
            domain.fuelGrade(),
            domain.unit(),
            domain.price(),
            domain.currency(),
            domain.source(),
            domain.fetchedAt()
        );
    }
}
