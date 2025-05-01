package com.fuelstation.managmentapi.fuelstation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import com.fuelstation.managmentapi.fuelstation.domain.FuelDeliveryService;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationRepository;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

@Component
public class ProcessFuelDelivery {

    @Autowired
    private FuelOrderRepository fuelOrderRepository;

    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Autowired
    private FuelDeliveryService fuelDeliveryService;

    @Autowired
    private DomainEventPublisher domainEventPublisher;
    

    public FuelStation process(long fuelOrderId) {
        FuelOrder fuelOrder = fuelOrderRepository.findById(fuelOrderId)
            .orElseThrow(() -> new IllegalArgumentException());

        FuelStation fuelStation = fuelStationRepository.findById(fuelOrder.getFuelStationId())
            .orElseThrow(() -> new IllegalArgumentException());

        fuelDeliveryService.processFuelDelivery(fuelStation, fuelOrder);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }
}
