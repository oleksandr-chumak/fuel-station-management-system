package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerByCredentialsId;
import com.fuelstation.managmentapi.manager.domain.Manager;

@Component
@AllArgsConstructor
public class AssignManagerToFuelStation {
    
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final GetManagerByCredentialsId getManagerByCredentialsId;
    private final GetFuelStationById getFuelStationById;

    @Transactional
    public Manager process(long fuelStationId, long managerId, Credentials credentials) {
        var fuelStation = getFuelStationById.process(fuelStationId, credentials);
        var manager = getManagerByCredentialsId.process(managerId);

        fuelStation.assignManager(manager.getCredentialsId());

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return manager;
    }

}