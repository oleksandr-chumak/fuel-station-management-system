package com.fuelstation.managmentapi.fuelorder.application.rest;

import com.fuelstation.managmentapi.common.application.exceptions.NotFoundException;

public class FuelOrderNotFoundException extends NotFoundException {

    public FuelOrderNotFoundException(Long orderId) {
        super("Fuel order with ID " + orderId + " not found", "FUEL_ORDER");
    }
    
}
