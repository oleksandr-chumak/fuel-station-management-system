package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;

import lombok.Getter;

/**
 * Base exception for all fuel station domain exceptions
 */
@Getter
public abstract class FuelStationDomainException extends DomainException {
    protected FuelStationDomainException(String message, String code) {
        super(message, "FUEL_STATION." + code);
    }

    protected FuelStationDomainException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }
}

