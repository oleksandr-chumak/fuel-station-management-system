package com.fuelstation.managmentapi.fuelprice.application.query.model;

import com.fuelstation.managmentapi.taxrule.application.rest.response.TaxRuleResponse;

import java.util.List;

public record TaxedFuelPriceResponse(
    FuelPriceResponse fuelPrice,
    List<TaxRuleResponse> taxRules
) {
}
