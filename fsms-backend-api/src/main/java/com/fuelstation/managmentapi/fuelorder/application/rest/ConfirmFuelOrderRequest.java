package com.fuelstation.managmentapi.fuelorder.application.rest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ConfirmFuelOrderRequest(
    @NotNull(message = "Price per liter must not be null")
    @Positive(message = "Price per liter must be positive")
    BigDecimal pricePerLiter
) {
}
