package com.fuelstation.managmentapi.fuelsale.domain;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelSale {
    private Long fuelSaleId;
    private Long fuelStationId;
    private Long fuelTankId;
    private FuelGrade fuelGrade;
    private BigDecimal volume;
    private BigDecimal pricePerLiter;
    private CurrencyCode currency;
    private BigDecimal totalRevenue;
    private OffsetDateTime soldAt;
}
