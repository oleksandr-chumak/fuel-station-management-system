package com.fuelstation.managmentapi.manager.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.ManagerService;

@Component
public class CreateManager {
    
    @Autowired
    private ManagerService managerService;
    
    public Manager process(String firstName, String lastName, String email) {
        return managerService.createManager(firstName, lastName, email);
    }
}