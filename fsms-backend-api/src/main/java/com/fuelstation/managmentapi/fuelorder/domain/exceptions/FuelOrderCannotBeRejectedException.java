package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;

public class FuelOrderCannotBeRejectedException extends FuelOrderDomainException {
    
    public FuelOrderCannotBeRejectedException(Long orderId, FuelOrderStatus currentStatus) {
        super(
            "Cannot reject fuel order with ID " + orderId + 
            " because its current status is '" + currentStatus.toString() + "'. Only pending orders can be rejected.",
            "CANNOT_REJECT"
        );
    }
}
