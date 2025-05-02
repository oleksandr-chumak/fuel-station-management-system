package com.fuelstation.managmentapi.manager.domain.exceptions;

import lombok.Getter;

@Getter
public class ManagerNotFoundException extends ManagerDomainException {

    private long managerId;

    public ManagerNotFoundException(long managerId) {
        super("Manager with ID " + managerId + " not found", "NOT_FOUND");
        this.managerId = managerId;
    }
    
}
