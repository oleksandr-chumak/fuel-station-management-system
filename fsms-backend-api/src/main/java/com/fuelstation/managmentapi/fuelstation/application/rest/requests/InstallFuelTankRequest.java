package com.fuelstation.managmentapi.fuelstation.application.rest.requests;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallFuelTankRequest {

    @NotNull(message = "Fuel grade must not be null")
    private FuelGrade fuelGrade;

    @NotNull(message = "Max capacity must not be null")
    @DecimalMin(value = "0.001", message = "Max capacity must be greater than zero")
    private BigDecimal maxCapacity;
}
