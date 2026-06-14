package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelorder.application.query.GetFuelOrderByIdQuery;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import com.fuelstation.managmentapi.fuelstation.application.query.GetActiveFuelStationByIdQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.FuelDeliveryService;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;

@Component
@AllArgsConstructor
public class ProcessFuelDelivery {

    private final FuelStationRepository fuelStationRepository;
    private final FuelOrderRepository fuelOrderRepository;
    private final FuelDeliveryService fuelDeliveryService;
    private final DomainEventPublisher domainEventPublisher;
    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;
    private final GetFuelOrderByIdQuery getFuelOrderByIdQuery;

    @Transactional
    public FuelStation process(long fuelOrderId) {
        var fuelOrder = getFuelOrderByIdQuery.process(fuelOrderId);
        var fuelStation = getActiveFuelStationByIdQuery.process(fuelOrder.getFuelStationId(), Actor.system());

        fuelDeliveryService.processFuelDelivery(fuelStation, fuelOrder);
        fuelOrder.process(Actor.system());

        fuelStationRepository.save(fuelStation);
        fuelOrderRepository.save(fuelOrder);

        domainEventPublisher.publishAll(fuelOrder.getDomainEvents());

        return fuelStation;
    }

}
