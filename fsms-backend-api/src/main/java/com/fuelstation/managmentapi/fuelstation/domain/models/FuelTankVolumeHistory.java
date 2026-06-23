package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;

import java.math.BigDecimal;
import java.time.Instant;

public record FuelTankVolumeHistory(
        Long historyId,
        long fuelStationId,
        long fuelTankId,
        FuelGrade fuelGrade,
        BigDecimal oldVolume,
        BigDecimal newVolume,
        FuelTankVolumeChangeReason reason,
        Actor performedBy,
        Instant changedAt
) {}
