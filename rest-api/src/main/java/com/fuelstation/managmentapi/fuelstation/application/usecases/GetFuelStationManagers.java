package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class GetFuelStationManagers {
   
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private GetFuelStationById getFuelStationById;

    public List<Manager> process(long fuelStationId) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId);
        List<Long> assignedManagerIds = fuelStation.getAssignedManagersIds(); 
        return managerRepository.findManagersByIds(assignedManagerIds);
    }
}
