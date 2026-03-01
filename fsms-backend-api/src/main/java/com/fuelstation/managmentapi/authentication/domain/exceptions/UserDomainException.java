package com.fuelstation.managmentapi.authentication.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.http.HttpStatus;

public abstract class UserDomainException extends DomainException {

    public UserDomainException(String message, String code) {
        super(message, HttpStatus.CONFLICT, "USER_" + code);
    }

    public UserDomainException(String message, HttpStatus httpStatus, String code) {
        super(message, httpStatus, "USER_" + code);
    }
}
