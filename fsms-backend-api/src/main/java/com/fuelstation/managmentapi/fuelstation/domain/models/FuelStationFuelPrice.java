package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.FuelGrade;

import java.math.BigDecimal;

public record FuelStationFuelPrice(FuelGrade fuelGrade, BigDecimal pricePerLiter, CurrencyCode currency) {}