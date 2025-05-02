package com.fuelstation.managmentapi.manager.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fuelstation.managmentapi.common.application.models.ErrorResponseEntity;
import com.fuelstation.managmentapi.manager.domain.exceptions.ManagerAlreadyTerminatedException;
import com.fuelstation.managmentapi.manager.domain.exceptions.ManagerDomainException;
import com.fuelstation.managmentapi.manager.domain.exceptions.ManagerNotFoundException;

@ControllerAdvice
public class ManagerExceptionHandler {

    @ExceptionHandler({
        ManagerNotFoundException.class
    })
    public ErrorResponseEntity handleNotFound(ManagerDomainException ex) {
        return ErrorResponseEntity.fromDomain(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        ManagerAlreadyTerminatedException.class
    })
    public ErrorResponseEntity handleConflict(ManagerDomainException ex) {
        return ErrorResponseEntity.fromDomain(ex, HttpStatus.CONFLICT);
    }
}
