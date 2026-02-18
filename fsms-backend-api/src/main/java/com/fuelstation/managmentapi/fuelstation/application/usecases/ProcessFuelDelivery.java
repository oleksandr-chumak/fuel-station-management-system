package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.FuelDeliveryService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
@AllArgsConstructor
public class ProcessFuelDelivery {

    private final FuelStationRepository fuelStationRepository;
    private final FuelOrderRepository fuelOrderRepository;
    private final FuelDeliveryService fuelDeliveryService;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelStation process(long fuelOrderId) {
        var fuelOrder = fuelOrderRepository.findById(fuelOrderId).get();
        var fuelStation = fuelStationRepository.findById(fuelOrder.getFuelStationId()).get();

        fuelDeliveryService.processFuelDelivery(fuelStation, fuelOrder);
        fuelOrder.process();

        fuelStationRepository.save(fuelStation);
        fuelOrderRepository.save(fuelOrder);

        domainEventPublisher.publishAll(fuelOrder.getDomainEvents());

        return fuelStation;
    }

}
