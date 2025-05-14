package com.fuelstation.managmentapi.fuelorder.domain;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

public interface FuelOrderFactory {
    public FuelOrder create(Long fuelStationId, FuelGrade fuelGrade, float amount);
}
