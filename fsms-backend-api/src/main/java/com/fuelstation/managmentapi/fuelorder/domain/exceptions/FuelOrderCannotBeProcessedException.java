package com.fuelstation.managmentapi.fuelorder.domain.exceptions;

import com.fuelstation.managmentapi.fuelorder.domain.FuelOrderStatus;

public class FuelOrderCannotBeProcessedException extends FuelOrderDomainException {

    public FuelOrderCannotBeProcessedException(long orderId, FuelOrderStatus currentStatus) {
        super(
                "Cannot process fuel order with ID " + orderId +
                        " because its current status is '" + currentStatus.toString() + ". Only confirmed orders can be processed.",
                "CANNOT_PROCESS"
        );
    }
}
