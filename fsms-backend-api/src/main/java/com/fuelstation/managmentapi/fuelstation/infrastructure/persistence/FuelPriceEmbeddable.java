package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelPriceEmbeddable {
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_grade", nullable = false)
    private FuelGrade fuelGrade;

    @Column(name = "price_per_liter", nullable = false)
    private BigDecimal pricePerLiter;
}
