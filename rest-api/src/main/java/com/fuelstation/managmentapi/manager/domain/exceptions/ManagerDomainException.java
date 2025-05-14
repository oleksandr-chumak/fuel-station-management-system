package com.fuelstation.managmentapi.manager.domain.exceptions;

import com.fuelstation.managmentapi.common.domain.DomainException;

public class ManagerDomainException extends DomainException  {

    public ManagerDomainException(String message, String code) {
        super(message, "MANAGER." + code);
    }
    
}
