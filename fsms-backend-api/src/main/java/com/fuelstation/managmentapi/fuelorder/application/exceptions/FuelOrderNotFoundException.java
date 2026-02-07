package com.fuelstation.managmentapi.fuelorder.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FuelOrderNotFoundException extends RuntimeException {

    public FuelOrderNotFoundException(Long orderId) {
        super("Fuel order with ID " + orderId + " not found");
    }
    
}
