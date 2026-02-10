package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
@AllArgsConstructor
public class GetFuelStationManagers {
   
    private final ManagerRepository managerRepository;
    private final GetFuelStationById getFuelStationById;

    @Transactional()
    public List<Manager> process(long fuelStationId, Credentials credentials) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId, credentials);
        List<Long> assignedManagerIds = fuelStation.getAssignedManagersIds(); 
        return managerRepository.findManagersByIds(assignedManagerIds);
    }
}
