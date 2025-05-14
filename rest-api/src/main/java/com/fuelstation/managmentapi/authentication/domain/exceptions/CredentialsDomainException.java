package com.fuelstation.managmentapi.authentication.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;

public abstract class CredentialsDomainException extends DomainException {

    public CredentialsDomainException(String message, String code) {
        super(message, "CREDENTIALS." + code);
    }
    
}
