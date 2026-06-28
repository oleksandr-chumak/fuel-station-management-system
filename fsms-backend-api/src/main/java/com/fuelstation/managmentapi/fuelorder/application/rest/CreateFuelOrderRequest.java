package com.fuelstation.managmentapi.fuelorder.application.rest;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderAllocation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CreateFuelOrderRequest(
    @NotNull(message = "Fuel station id must not be null")
    Long fuelStationId,

    @NotNull(message = "Fuel grade must not be null")
    FuelGrade fuelGrade,

    @Valid
    @NotEmpty(message = "Must include at least one fuel tank")
    List<AllocationRequest> allocations
) {

    public record AllocationRequest(
        @NotNull(message = "Fuel Tank Id must not be null")
        Long fuelTankId,

        @NotNull(message = "Amount must not be null")
        @Positive(message = "Amount should be a positive value")
        BigDecimal volume
    ) {

        public FuelOrderAllocation toDomain() {
            return new FuelOrderAllocation(
                fuelTankId,
                volume
            );
        }

    }

}