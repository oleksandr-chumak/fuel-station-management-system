package com.fuelstation.managmentapi.manager.application.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.http.HttpStatus;

public class ManagerDomainException extends DomainException {

    protected ManagerDomainException(String message, String code) {
        super(message, HttpStatus.CONFLICT, "MANAGER_" + code);
    }

    protected ManagerDomainException(String message, HttpStatus httpStatus, String code) {
        super(message, httpStatus, "MANGER_" + code);
    }

}
