package com.fuelstation.managmentapi.manager.application.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class GetManagerFuelStations {
    
    @Autowired
    public ManagerRepository managerRepository;

    @Autowired
    public GetManagerById getManagerById;

    public List<FuelStation> process(long managerId) {
        getManagerById.process(managerId);
        return this.managerRepository.findManagerFuelStation(managerId);
    }
    
}
