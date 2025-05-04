package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelPriceEmbeddable {
    @Enumerated(EnumType.STRING)
    private FuelGrade fuelGrade;

    private float pricePerLiter;
}
