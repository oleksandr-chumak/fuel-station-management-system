package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerByCredentialsId;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class UnassignManagerFromFuelStation {
    
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final GetFuelStationById getFuelStationById;
    private final GetManagerByCredentialsId getManagerByCredentialsId;

    @Transactional
    public FuelStation process(long fuelStationId, long managerId, Credentials credentials) {
        var fuelStation = getFuelStationById.process(fuelStationId, credentials);
        var manager = getManagerByCredentialsId.process(managerId);

        fuelStation.unassignManager(manager.getCredentialsId());

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }
}
