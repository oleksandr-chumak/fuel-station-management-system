package com.fuelstation.managmentapi.fuelstation.application.usecases;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.application.usecases.GetFuelOrderById;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.domain.FuelDeliveryService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
public class ProcessFuelDelivery {

    private final FuelStationRepository fuelStationRepository;

    private final FuelDeliveryService fuelDeliveryService;

    private final DomainEventPublisher domainEventPublisher;

    private final GetFuelStationById getFuelStationById;
    
    private final GetFuelOrderById getFuelOrderById;

    public ProcessFuelDelivery(FuelStationRepository fuelStationRepository, FuelDeliveryService fuelDeliveryService, DomainEventPublisher domainEventPublisher, GetFuelStationById getFuelStationById, GetFuelOrderById getFuelOrderById) {
        this.fuelStationRepository = fuelStationRepository;
        this.fuelDeliveryService = fuelDeliveryService;
        this.domainEventPublisher = domainEventPublisher;
        this.getFuelStationById = getFuelStationById;
        this.getFuelOrderById = getFuelOrderById;
    }

    @Transactional
    public FuelStation process(long fuelOrderId) {
        FuelOrder fuelOrder = getFuelOrderById.process(fuelOrderId);
        FuelStation fuelStation = getFuelStationById.process(fuelOrder.getFuelStationId());

        fuelDeliveryService.processFuelDelivery(fuelStation, fuelOrder);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }
}
