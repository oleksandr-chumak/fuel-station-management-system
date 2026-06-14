package com.fuelstation.managmentapi.fuelstation.application.rest.requests;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFuelPricesBulkRequest {

    @NotEmpty(message = "Prices must not be empty")
    @Valid
    private List<FuelPriceUpdate> prices;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FuelPriceUpdate {

        @NotNull(message = "Fuel grade must not be null")
        private FuelGrade fuelGrade;

        @NotNull(message = "New price must not be null")
        @Positive(message = "New price must be a positive number")
        private BigDecimal newPrice;
    }
}
