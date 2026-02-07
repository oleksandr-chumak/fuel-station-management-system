package com.fuelstation.managmentapi.fuelstation.application.usecases;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerById;
import com.fuelstation.managmentapi.manager.domain.Manager;

@Component
public class AssignManagerToFuelStation {
    
    private final FuelStationRepository fuelStationRepository;

    private final DomainEventPublisher domainEventPublisher;
    
    private final GetManagerById getManagerById;

    private final GetFuelStationById getFuelStationById;

    public AssignManagerToFuelStation(FuelStationRepository fuelStationRepository, DomainEventPublisher domainEventPublisher, GetManagerById getManagerById, GetFuelStationById getFuelStationById) {
        this.fuelStationRepository = fuelStationRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.getManagerById = getManagerById;
        this.getFuelStationById = getFuelStationById;
    }

    @Transactional
    public FuelStation process(long fuelStationId, long managerId) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId); 
        Manager manager = getManagerById.process(managerId);

        fuelStation.assignManager(manager.getId());

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }

}