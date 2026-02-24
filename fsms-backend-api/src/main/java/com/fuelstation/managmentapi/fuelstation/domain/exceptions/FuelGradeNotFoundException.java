package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a specific fuel grade is not found at a fuel station
 */
@Getter
public class FuelGradeNotFoundException extends FuelStationDomainException {
    private final String fuelGrade;
    private final long fuelStationId;

    public FuelGradeNotFoundException(String fuelGrade, long fuelStationId) {
        super(
            String.format("Fuel grade %s not found in fuel station %d", fuelGrade, fuelStationId),
            HttpStatus.NOT_FOUND,
            "FUEL_GRADE_NOT_FOUND"
        );
        this.fuelGrade = fuelGrade;
        this.fuelStationId = fuelStationId;
    }
}
