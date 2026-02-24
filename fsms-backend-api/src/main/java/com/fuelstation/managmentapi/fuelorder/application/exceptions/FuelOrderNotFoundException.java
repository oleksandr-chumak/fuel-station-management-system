package com.fuelstation.managmentapi.fuelorder.application.exceptions;

import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FuelOrderNotFoundException extends FuelOrderDomainException {

    public FuelOrderNotFoundException(Long orderId) {
        super(
                "Fuel order with ID " + orderId + " not found",
                HttpStatus.NOT_FOUND,
                "NOT_FOUND"
        );
    }
    
}
