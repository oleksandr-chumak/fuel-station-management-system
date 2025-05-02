package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;

public abstract class FuelOrderDomainException extends DomainException {

    public FuelOrderDomainException(String message, String code) {
        super(message, "FUEL_STATION" + code);
    }
    
}
