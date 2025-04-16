package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import java.time.LocalDate;

public record FuelTank(
    Long id,
    FuelGrade fuelGrade,
    float currentVolume,
    int maxCapacity,
    LocalDate lastRefilDate
) {}