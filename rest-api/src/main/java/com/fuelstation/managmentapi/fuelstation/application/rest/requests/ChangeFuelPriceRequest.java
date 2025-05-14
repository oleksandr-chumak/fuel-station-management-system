package com.fuelstation.managmentapi.fuelstation.application.rest.requests;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFuelPriceRequest {

    @NotNull(message =  "Fuel grade must not be null")
    private FuelGrade fuelGrade;

    @Positive(message = "New price must be a positive number")
    private float newPrice;

}