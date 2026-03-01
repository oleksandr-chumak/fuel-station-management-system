package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.application.UserFetcher;
import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationAccessControlChecker;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationFetcher;
import com.fuelstation.managmentapi.manager.application.support.ManagerFetcher;
import com.fuelstation.managmentapi.manager.domain.Manager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class UnassignManagerFromFuelStation {
    
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final ManagerFetcher managerFetcher;
    private final FuelStationFetcher fuelStationFetcher;
    private final FuelStationAccessControlChecker accessControlChecker;
    private final UserFetcher userFetcher;

    @Transactional
    public Manager process(long fuelStationId, long managerId, Actor performedBy) {
        var fuelStation = fuelStationFetcher.fetchActiveById(fuelStationId);
        var manager = managerFetcher.fetchById(managerId);
        var credentials = userFetcher.fetchById(performedBy.id());
        accessControlChecker.checkAccess(fuelStation, credentials);

        fuelStation.unassignManager(manager.getManagerId(), performedBy);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return manager;
    }
}
