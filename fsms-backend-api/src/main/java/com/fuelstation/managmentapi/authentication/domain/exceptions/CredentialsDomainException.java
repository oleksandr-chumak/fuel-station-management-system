package com.fuelstation.managmentapi.authentication.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.http.HttpStatus;

public abstract class CredentialsDomainException extends DomainException {

    public CredentialsDomainException(String message, String code) {
        super(message, HttpStatus.CONFLICT, "CREDENTIALS_" + code);
    }

    public CredentialsDomainException(String message, HttpStatus httpStatus, String code) {
        super(message, httpStatus, "CREDENTIALS_" + code);
    }
}
