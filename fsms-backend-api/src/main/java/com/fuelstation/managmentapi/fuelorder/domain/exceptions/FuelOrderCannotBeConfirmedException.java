package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;

public class FuelOrderCannotBeConfirmedException extends FuelOrderDomainException {
    
    public FuelOrderCannotBeConfirmedException(Long orderId, FuelOrderStatus currentStatus) {
        super(
            "Cannot confirm fuel order with ID " + orderId + 
            " because its current status is '" + currentStatus.toString() + "'. Only pending orders can be confirmed.",
            "CANNOT_CONFIRM"
        );
    }
}