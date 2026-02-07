package com.fuelstation.managmentapi.common.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fuelstation.managmentapi.common.application.models.ErrorResponseEntity;
import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ErrorResponseEntity handleDomainException(DomainException ex) {
        return ErrorResponseEntity.fromDomain(ex, HttpStatus.CONFLICT);
    }

}