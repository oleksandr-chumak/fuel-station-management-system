package com.fuelstation.managmentapi.fuelstation.application.usecases;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class ChangeFuelPrice {
   
    private final FuelStationRepository fuelStationRepository;

    private final DomainEventPublisher domainEventPublisher;
    
    private final GetFuelStationById getFuelStationById;

    public ChangeFuelPrice(FuelStationRepository fuelStationRepository, DomainEventPublisher domainEventPublisher, GetFuelStationById getFuelStationById) {
        this.fuelStationRepository = fuelStationRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.getFuelStationById = getFuelStationById;
    }

    @Transactional
    public FuelStation process(long fuelStationId, FuelGrade fuelGrade, float newPrice) {
        FuelStation fuelStation = getFuelStationById.process(fuelStationId);

        fuelStation.changeFuelPrice(fuelGrade, newPrice);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }

}
