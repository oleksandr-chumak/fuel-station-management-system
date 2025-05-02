package com.fuelstation.managmentapi.fuelorder.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fuelstation.managmentapi.common.application.models.ErrorResponseEntity;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderAmountExceedsLimitException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeConfirmedException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderCannotBeRejectedException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderDomainException;
import com.fuelstation.managmentapi.fuelorder.domain.exceptions.FuelOrderNotFoundException;

@ControllerAdvice
public class FuelOrderExceptionHandler {

    @ExceptionHandler({
        FuelOrderNotFoundException.class
    })
    public ErrorResponseEntity handleNotFound(FuelOrderDomainException ex) {
        return ErrorResponseEntity.fromDomain(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        FuelOrderCannotBeConfirmedException.class,
        FuelOrderCannotBeRejectedException.class,
        FuelOrderAmountExceedsLimitException.class
    })
    public ErrorResponseEntity handleConflict(FuelOrderDomainException ex) {
        return ErrorResponseEntity.fromDomain(ex, HttpStatus.CONFLICT);
    }
}
