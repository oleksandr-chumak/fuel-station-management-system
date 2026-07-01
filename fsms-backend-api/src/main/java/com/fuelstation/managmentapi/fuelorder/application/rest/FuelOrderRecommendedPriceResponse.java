package com.fuelstation.managmentapi.fuelorder.application.rest;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.fuelorder.application.query.GetFuelOrderRecommendedPriceQuery;

import java.math.BigDecimal;

public record FuelOrderRecommendedPriceResponse(
    BigDecimal pricePerLiter,
    CurrencyCode currency,
    String fuelGrade
) {
    public static FuelOrderRecommendedPriceResponse fromResult(GetFuelOrderRecommendedPriceQuery.Result result) {
        return new FuelOrderRecommendedPriceResponse(
            result.pricePerLiter(),
            result.currency(),
            result.fuelGrade().toString()
        );
    }
}
