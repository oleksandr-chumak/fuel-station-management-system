package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelPriceEmbeddable {
    @Column(name = "fuel_grade_id", nullable = false)
    private Long fuelGradeId;

    @Column(name = "price_per_liter", nullable = false)
    private BigDecimal pricePerLiter;
}
