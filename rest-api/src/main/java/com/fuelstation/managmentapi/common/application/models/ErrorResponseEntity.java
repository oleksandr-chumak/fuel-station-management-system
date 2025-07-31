package com.fuelstation.managmentapi.common.application.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fuelstation.managmentapi.common.domain.DomainException;

public class ErrorResponseEntity extends ResponseEntity<ErrorResponse> {

    public ErrorResponseEntity(String message, String code, HttpStatus status) {
        super(new ErrorResponse(message, code, status), status);
    }

    public static ErrorResponseEntity fromDomain(DomainException exception, HttpStatus status) {
        return new ErrorResponseEntity(exception.getMessage(), exception.getCode(), status);
    }

}
