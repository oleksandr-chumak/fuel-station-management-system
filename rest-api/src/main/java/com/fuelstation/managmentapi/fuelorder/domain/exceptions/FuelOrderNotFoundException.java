package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

public class FuelOrderNotFoundException extends FuelOrderDomainException {

    public FuelOrderNotFoundException(Long orderId) {
        super("Fuel order with ID " + orderId + " not found", "NOT_FOUND");
    }
}
