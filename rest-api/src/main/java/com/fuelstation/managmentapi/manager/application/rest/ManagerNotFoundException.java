package com.fuelstation.managmentapi.manager.application.rest;

import com.fuelstation.managmentapi.common.application.exceptions.NotFoundException;

import lombok.Getter;

@Getter
public class ManagerNotFoundException extends NotFoundException {

    private long managerId;

    public ManagerNotFoundException(long managerId) {
        super("Manager with ID " + managerId + " not found", "MANAGER");
        this.managerId = managerId;
    }
    
}
