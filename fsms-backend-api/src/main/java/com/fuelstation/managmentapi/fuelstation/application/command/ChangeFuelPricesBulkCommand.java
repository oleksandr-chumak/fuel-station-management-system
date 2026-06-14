package com.fuelstation.managmentapi.fuelstation.application.command;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;

import java.math.BigDecimal;
import java.util.List;

public record ChangeFuelPricesBulkCommand(
        long fuelStationId,
        List<FuelPriceChange> changes,
        Actor performedBy
) {
    public record FuelPriceChange(FuelGrade fuelGrade, BigDecimal newPrice) {}
}
