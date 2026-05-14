package com.fuelstation.managmentapi.fuelpurchase.application.rest;

import com.fuelstation.managmentapi.fuelpurchase.domain.FuelPurchase;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record FuelPurchaseResponse(
        Long fuelPurchaseId,
        Long fuelOrderId,
        Long fuelStationId,
        String fuelGrade,
        BigDecimal amount,
        BigDecimal pricePerLiter,
        String currency,
        BigDecimal totalPrice,
        OffsetDateTime purchasedAt
) {
    public static FuelPurchaseResponse from(FuelPurchase domain) {
        return new FuelPurchaseResponse(
                domain.getFuelPurchaseId(),
                domain.getFuelOrderId(),
                domain.getFuelStationId(),
                domain.getFuelGrade().toString(),
                domain.getAmount(),
                domain.getPricePerLiter(),
                domain.getCurrency().name(),
                domain.getTotalPrice(),
                domain.getPurchasedAt()
        );
    }
}
