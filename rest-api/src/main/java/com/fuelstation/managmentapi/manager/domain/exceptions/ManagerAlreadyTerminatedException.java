package com.fuelstation.managmentapi.manager.domain.exceptions;

public class ManagerAlreadyTerminatedException extends ManagerDomainException {
    
    public ManagerAlreadyTerminatedException(Long managerId) {
        super("Manager with ID " + managerId + " is already terminated", "ALREADY_TERMINATED");
    }
}
