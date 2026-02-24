package com.fuelstation.managmentapi.common.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class DomainException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;

    protected DomainException(String message, HttpStatus httpStatus, String code) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

}