package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelTank{
    private Long id;
    private FuelGrade fuelGrade;
    private BigDecimal currentVolume;
    private BigDecimal maxCapacity;
    private Optional<OffsetDateTime> lastRefillDate;

    public BigDecimal getAvailableVolume() {
        return this.maxCapacity.subtract(this.currentVolume);
    }
}