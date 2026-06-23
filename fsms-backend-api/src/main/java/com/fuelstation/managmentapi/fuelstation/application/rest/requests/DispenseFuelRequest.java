package com.fuelstation.managmentapi.fuelstation.application.rest.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispenseFuelRequest {

    @NotNull(message = "Volume must not be null")
    @DecimalMin(value = "0.001", message = "Volume must be greater than zero")
    private BigDecimal volume;
}
