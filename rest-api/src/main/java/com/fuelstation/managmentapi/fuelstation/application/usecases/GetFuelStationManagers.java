package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class GetFuelStationManagers {
   
    private final ManagerRepository managerRepository;

    private final GetFuelStationById getFuelStationById;

    public GetFuelStationManagers(ManagerRepository managerRepository, GetFuelStationById getFuelStationById) {
        this.managerRepository = managerRepository;
        this.getFuelStationById = getFuelStationById;
    }

    @Transactional()
    public List<Manager> process(long fuelStationId) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId);
        List<Long> assignedManagerIds = fuelStation.getAssignedManagersIds(); 
        return managerRepository.findManagersByIds(assignedManagerIds);
    }
}
