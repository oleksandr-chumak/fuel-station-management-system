package com.fuelstation.managmentapi.fuelorder.domain;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FuelOrderAllocation(
    Long fuelTankId,
    @NotNull
    BigDecimal volume
) {
}
