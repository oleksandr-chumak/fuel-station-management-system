package com.fuelstation.managmentapi.fuelstation.application.command;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;

import java.math.BigDecimal;

public record ChangeFuelPriceCommand(
        long fuelStationId,
        FuelGrade fuelGrade,
        BigDecimal newPrice,
        Actor performedBy
) {}
