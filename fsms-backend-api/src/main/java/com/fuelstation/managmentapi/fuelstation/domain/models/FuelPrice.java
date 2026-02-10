package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import java.math.BigDecimal;

public record FuelPrice(FuelGrade fuelGrade, BigDecimal pricePerLiter) {}