package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.http.HttpStatus;

public abstract class FuelOrderDomainException extends DomainException {

    public FuelOrderDomainException(String message, String code) {
        super(message, HttpStatus.CONFLICT, "FUEL_ORDER_" + code);
    }

    public FuelOrderDomainException(String message,HttpStatus status, String code) {
        super(message, status, "FUEL_ORDER_" + code);
    }

}
