package com.fuelstation.managmentapi.common.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        var error = new ErrorResponse(
                ex.getMessage(),
                ex.getCode(),
                ex.getHttpStatus()
        );
        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

}