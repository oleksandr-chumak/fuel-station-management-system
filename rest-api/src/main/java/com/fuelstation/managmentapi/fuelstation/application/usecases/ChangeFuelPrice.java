package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class ChangeFuelPrice {
   
    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public FuelStation process(long fuelStationId, FuelGrade fuelGrade, float newPrice) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId)
            .orElseThrow(() -> new FuelStationNotFoundException(fuelStationId));
        fuelStation.changeFuelPrice(fuelGrade, newPrice);
        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());
        return fuelStation;
    }

}
