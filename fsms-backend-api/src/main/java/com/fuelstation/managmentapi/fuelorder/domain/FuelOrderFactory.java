package com.fuelstation.managmentapi.fuelorder.domain;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

import java.math.BigDecimal;

public interface FuelOrderFactory {
    FuelOrder create(Long fuelStationId, FuelGrade fuelGrade, BigDecimal amount);
}
