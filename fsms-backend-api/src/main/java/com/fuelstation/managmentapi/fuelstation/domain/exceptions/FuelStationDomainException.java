package com.fuelstation.managmentapi.fuelstation.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception for all fuel station domain exceptions
 */
@Getter
public abstract class FuelStationDomainException extends DomainException {

    public FuelStationDomainException(String message, String code) {
        super(message, HttpStatus.CONFLICT, "FUEL_STATION_" + code);
    }

    public FuelStationDomainException(String message,HttpStatus httpStatus, String code) {
        super(message, httpStatus, "FUEL_STATION_" + code);
    }

}

