package com.fuelstation.managmentapi.manager.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.application.rest.ManagerNotFoundException;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class GetManagerById {
    
    @Autowired
    private ManagerRepository managerRepository;
    
    public Manager process(long managerId) {
        return managerRepository.findById(managerId)
            .orElseThrow(() -> new ManagerNotFoundException(managerId));
    }
}
