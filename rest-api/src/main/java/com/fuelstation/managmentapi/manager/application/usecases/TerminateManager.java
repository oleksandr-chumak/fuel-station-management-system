package com.fuelstation.managmentapi.manager.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.ManagerService;

@Component
public class TerminateManager {
    
    @Autowired
    private ManagerService managerService;
    
    public Manager process(Long managerId) {
        return managerService.terminateManager(managerId);
    }
}