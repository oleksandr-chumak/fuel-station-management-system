package com.fuelstation.managmentapi.fuelprice.application.query.model;

import java.util.List;

public record TaxedFuelPriceResponse(
    FuelPriceResponse fuelPrice,
    List<FuelTaxRuleResponse> taxRules
) {
}
