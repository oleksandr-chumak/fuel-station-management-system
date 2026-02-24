package com.fuelstation.managmentapi.manager.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;
import org.springframework.http.HttpStatus;

public class ManagerDomainException extends DomainException  {

    public ManagerDomainException(String message, String code) {
        super(message, HttpStatus.CONFLICT, "MANAGER_" + code);
    }
    
}
