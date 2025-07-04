package com.fuelstation.managmentapi.manager.application.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class GetAllManagers {
    
    private final ManagerRepository managerRepository;

    public GetAllManagers(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<Manager> process() {
        return managerRepository.findAll();
    }

}
