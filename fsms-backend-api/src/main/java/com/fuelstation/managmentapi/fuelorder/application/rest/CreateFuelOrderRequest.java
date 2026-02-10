package com.fuelstation.managmentapi.fuelorder.application.rest;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFuelOrderRequest {

    @NotNull(message = "Fuel station id must not be null")
    private Long fuelStationId;

    @NotNull(message = "Fuel grade must not be null")
    private FuelGrade fuelGrade;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be a positive number")
    private BigDecimal amount;

}