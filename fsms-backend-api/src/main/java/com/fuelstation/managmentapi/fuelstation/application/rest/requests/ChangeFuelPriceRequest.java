package com.fuelstation.managmentapi.fuelstation.application.rest.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFuelPriceRequest {

    @NotNull(message = "New price must not be null")
    @Positive(message = "New price must be a positive number")
    private BigDecimal newPrice;

}
