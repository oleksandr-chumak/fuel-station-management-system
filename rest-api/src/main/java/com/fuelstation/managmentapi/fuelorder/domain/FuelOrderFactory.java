package com.fuelstation.managmentapi.fuelorder.domain;

import com.fuelstation.managmentapi.common.domain.FuelGrade;

public interface FuelOrderFactory {
    /*
     * The ordered amount of fuel can't be greater than the available space in the fuel tanks for the specified fuel grade, 
     * minus the amount of fuel already ordered for that grade which hasn't been confirmed or rejected.
     */
    public FuelOrder create(Long fuelStationId, FuelGrade fuelGrade, float amount);
}
