package com.fuelstation.managmentapi.manager.application.usecases;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.domain.ManagerRepository;

@Component
public class GetManagerFuelStations {
    
    @Autowired
    public ManagerRepository managerRepository;

    public List<FuelStation> process(long managerId) {
        managerRepository.findById(managerId).orElseThrow(() -> new NoSuchElementException("Manager with id: " + managerId + " doesn't exist"));
        return this.managerRepository.findManagerFuelStation(managerId);
    }
    
}
