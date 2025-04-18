package com.fuelstation.managmentapi.manager.application.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.ManagerRepository;

@Component
public class GetAllManagers {
    
    @Autowired
    private ManagerRepository managerRepository;

    public List<Manager> process() {
        return managerRepository.findAll();
    }

}
