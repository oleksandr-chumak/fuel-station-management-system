package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import org.springframework.http.HttpStatus;

public class FuelOrderDuplicateAllocationException extends FuelOrderDomainException {
    public FuelOrderDuplicateAllocationException() {
        super(
            "Fuel order allocations must not contain duplicate fuel tank IDs.",
            HttpStatus.BAD_REQUEST,
            "DUPLICATE_ALLOCATION"
        );
    }
}
