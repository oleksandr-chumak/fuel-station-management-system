package com.fuelstation.managmentapi.fuelstation.application.usecases;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationFactory;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class CreateFuelStation {

    private final FuelStationRepository fuelStationRepository;

    final FuelStationFactory fuelStationFactory;

    private final DomainEventPublisher domainEventPublisher;

    public CreateFuelStation(FuelStationRepository fuelStationRepository, FuelStationFactory fuelStationFactory, DomainEventPublisher domainEventPublisher) {
        this.fuelStationRepository = fuelStationRepository;
        this.fuelStationFactory = fuelStationFactory;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public FuelStation process(String street, String buildingNumber, String city, String postalCode, String country ) {
        FuelStation fuelStation = fuelStationFactory.create(street, buildingNumber, city, postalCode, country);

        FuelStation savedFuelStation = fuelStationRepository.save(fuelStation);
        domainEventPublisher.publish(new FuelStationCreated(savedFuelStation.getId()));

        return savedFuelStation;
    } 
    
}
