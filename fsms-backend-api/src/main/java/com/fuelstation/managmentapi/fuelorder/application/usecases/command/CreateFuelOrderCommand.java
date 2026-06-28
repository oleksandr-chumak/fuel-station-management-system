package com.fuelstation.managmentapi.fuelorder.application.usecases.command;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderAllocation;

import java.util.List;

public record CreateFuelOrderCommand(
    long fuelStationId,
    FuelGrade fuelGrade,
    List<FuelOrderAllocation> allocations,
    Actor performedBy
) {

}
