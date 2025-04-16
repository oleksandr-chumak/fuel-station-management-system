package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

public record FuelPrice(FuelGrade fuelGrade, float pricePerLiter) {}