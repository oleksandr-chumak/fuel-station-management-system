package com.fuelstation.managmentapi.fuelstation.application.usecases;

import java.util.List;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationAccessControlChecker;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationFetcher;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
@AllArgsConstructor
public class GetFuelStationManagers {
   
    private final ManagerRepository managerRepository;
    private final FuelStationFetcher fuelStationFetcher;
    private final FuelStationAccessControlChecker accessControlChecker;

    @Transactional()
    public List<Manager> process(long fuelStationId, User user) {
        var fuelStation = fuelStationFetcher.fetchActiveById(fuelStationId);
        accessControlChecker.checkAccess(fuelStation, user);
        List<Long> assignedManagerIds = fuelStation.getAssignedManagersIds();
        return managerRepository.findByIds(assignedManagerIds);
    }
}
