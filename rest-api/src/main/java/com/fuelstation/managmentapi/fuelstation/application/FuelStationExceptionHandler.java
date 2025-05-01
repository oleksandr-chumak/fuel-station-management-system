package com.fuelstation.managmentapi.fuelstation.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fuelstation.managmentapi.common.application.models.ErrorResponseEntity;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelGradeNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationAlreadyDeactivatedException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationDomainException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.ManagerAlreadyAssignedException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.TankCapacityExceededException;

@ControllerAdvice
public class FuelStationExceptionHandler {
    
    @ExceptionHandler({ 
        FuelGradeNotFoundException.class, 
        FuelStationNotFoundException.class
    }) 
    public ErrorResponseEntity handleNotFound(FuelStationDomainException ex) {
        return ErrorResponseEntity.fromDomain(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ 
        FuelStationAlreadyDeactivatedException.class, 
        ManagerAlreadyAssignedException.class,
        TankCapacityExceededException.class
    }) 
    public ErrorResponseEntity handleConflict(FuelStationDomainException ex) {
        return ErrorResponseEntity.fromDomain(ex, HttpStatus.CONFLICT);
    }
}
