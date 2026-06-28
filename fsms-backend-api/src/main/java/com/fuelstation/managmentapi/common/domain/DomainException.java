package com.fuelstation.managmentapi.common.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public abstract class DomainException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;

    protected DomainException(String message, HttpStatus httpStatus, String code) {
        this(message, httpStatus, code, Map.of());
    }

    protected DomainException(String message, HttpStatus httpStatus, String code, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.details = details == null ? Map.of() : Map.copyOf(details);
    }

}
