package com.fuelstation.managmentapi.fuelstation.application.usecases;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.application.usecases.GetFuelOrderById;
import com.fuelstation.managmentapi.fuelstation.domain.FuelDeliveryService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
@AllArgsConstructor
public class ProcessFuelDelivery {

    private final FuelStationRepository fuelStationRepository;
    private final FuelDeliveryService fuelDeliveryService;
    private final DomainEventPublisher domainEventPublisher;
    private final GetFuelOrderById getFuelOrderById;

    @Transactional
    public FuelStation process(long fuelOrderId) {
        var fuelOrder = getFuelOrderById.process(fuelOrderId);
        var fuelStation = fuelStationRepository.findById(fuelOrder.getFuelStationId()).get();

        fuelDeliveryService.processFuelDelivery(fuelStation, fuelOrder);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }
}
