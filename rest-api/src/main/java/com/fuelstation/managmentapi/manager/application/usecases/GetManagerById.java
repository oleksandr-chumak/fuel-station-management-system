package com.fuelstation.managmentapi.manager.application.usecases;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.application.exceptions.ManagerNotFoundException;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class GetManagerById {
    
    private final ManagerRepository managerRepository;

    public GetManagerById(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public Manager process(long managerId) {
        return managerRepository.findById(managerId)
            .orElseThrow(() -> new ManagerNotFoundException(managerId));
    }
}
