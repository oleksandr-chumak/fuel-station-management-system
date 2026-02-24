package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationAccessControlChecker;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationFetcher;
import com.fuelstation.managmentapi.manager.application.support.ManagerFetcher;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;
import com.fuelstation.managmentapi.manager.domain.Manager;

@Component
@AllArgsConstructor
public class AssignManagerToFuelStation {

    private final ManagerFetcher managerFetcher;
    private final FuelStationFetcher fuelStationFetcher;
    private final FuelStationAccessControlChecker accessControlChecker;
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public Manager process(long fuelStationId, long managerId, Credentials credentials) {
        var fuelStation = fuelStationFetcher.fetchActiveById(fuelStationId);
        var manager = managerFetcher.fetchById(managerId);
        accessControlChecker.checkAccess(fuelStation, credentials);

        fuelStation.assignManager(manager.getCredentialsId());

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return manager;
    }

}