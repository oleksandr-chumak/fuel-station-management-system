package com.fuelstation.managmentapi.fuelstation.application.usecases;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class DeactivateFuelStation {
    
    private final FuelStationRepository fuelStationRepository;

    private final DomainEventPublisher domainEventPublisher;
    
    private final GetFuelStationById getFuelStationById;

    public DeactivateFuelStation(FuelStationRepository fuelStationRepository, DomainEventPublisher domainEventPublisher, GetFuelStationById getFuelStationById) {
        this.fuelStationRepository = fuelStationRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.getFuelStationById = getFuelStationById;
    }

    @Transactional
    public FuelStation process(long fuelStationId) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId);

        fuelStation.deactivate();

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }

}
