package com.fuelstation.managmentapi.fuelsale.application.rest;

import com.fuelstation.managmentapi.fuelsale.domain.FuelSale;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record FuelSaleResponse(
        Long fuelSaleId,
        Long fuelStationId,
        Long fuelTankId,
        String fuelGrade,
        BigDecimal volume,
        BigDecimal pricePerLiter,
        String currency,
        BigDecimal totalRevenue,
        OffsetDateTime soldAt
) {
    public static FuelSaleResponse from(FuelSale domain) {
        return new FuelSaleResponse(
                domain.getFuelSaleId(),
                domain.getFuelStationId(),
                domain.getFuelTankId(),
                domain.getFuelGrade().toString(),
                domain.getVolume(),
                domain.getPricePerLiter(),
                domain.getCurrency().name(),
                domain.getTotalRevenue(),
                domain.getSoldAt()
        );
    }
}
