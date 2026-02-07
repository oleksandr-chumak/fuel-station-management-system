package com.fuelstation.managmentapi.common.domain;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
    private final String code;

    protected DomainException(String message, String code) {
        super(message);
        this.code = code;
    }

    protected DomainException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}