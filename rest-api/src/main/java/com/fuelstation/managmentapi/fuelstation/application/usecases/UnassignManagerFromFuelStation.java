package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerById;
import com.fuelstation.managmentapi.manager.domain.Manager;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UnassignManagerFromFuelStation {
    
    private final FuelStationRepository fuelStationRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final GetFuelStationById getFuelStationById;

    private final GetManagerById getManagerById;

    public UnassignManagerFromFuelStation(FuelStationRepository fuelStationRepository, DomainEventPublisher domainEventPublisher, GetFuelStationById getFuelStationById, GetManagerById getManagerById) {
        this.fuelStationRepository = fuelStationRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.getFuelStationById = getFuelStationById;
        this.getManagerById = getManagerById;
    }

    @Transactional
    public FuelStation process(long fuelStationId, long managerId) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId);
        Manager manager = getManagerById.process(managerId);

        fuelStation.unassignManager(manager.getId());

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }
}
