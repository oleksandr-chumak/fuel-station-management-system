package com.fuelstation.managmentapi.fuelpurchase.domain;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelPurchase {
    private Long fuelPurchaseId;
    private Long fuelOrderId;
    private Long fuelStationId;
    private FuelGrade fuelGrade;
    private BigDecimal amount;
    private BigDecimal pricePerLiter;
    private CurrencyCode currency;
    private BigDecimal totalPrice;
    private OffsetDateTime purchasedAt;
}
