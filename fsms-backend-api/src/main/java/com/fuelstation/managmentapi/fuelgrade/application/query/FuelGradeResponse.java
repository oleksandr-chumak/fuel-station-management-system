package com.fuelstation.managmentapi.fuelgrade.application.query;

import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;

public record FuelGradeResponse(
        Long fuelGradeId,
        FuelGrade code
) {
    public static FuelGradeResponse fromDomain(FuelGrade fuelGrade) {
        return new FuelGradeResponse(fuelGrade.getId(), fuelGrade);
    }
}
