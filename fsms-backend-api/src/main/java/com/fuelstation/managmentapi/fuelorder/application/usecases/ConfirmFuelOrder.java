package com.fuelstation.managmentapi.fuelorder.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.application.query.GetFuelOrderByIdQuery;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelorder.infrastructure.persistence.FuelOrderRepository;
import com.fuelstation.managmentapi.fuelstation.application.query.GetFuelStationByIdQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class ConfirmFuelOrder {

    private final FuelOrderRepository fuelOrderRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final GetFuelOrderByIdQuery getFuelOrderByIdQuery;
    private final GetFuelStationByIdQuery getFuelStationByIdQuery;

    @Transactional
    public FuelOrder process(long fuelOrderId, BigDecimal pricePerLiter, Actor performedBy) {
        FuelOrder fuelOrder = getFuelOrderByIdQuery.process(fuelOrderId);
        var fuelStation = getFuelStationByIdQuery.process(fuelOrder.getFuelStationId(), performedBy);
        var currency = CurrencyCode.fromCountryCode(fuelStation.getAddress().country());

        fuelOrder.confirm(performedBy, pricePerLiter, currency);

        fuelOrderRepository.save(fuelOrder);
        domainEventPublisher.publishAll(fuelOrder.getDomainEvents());

        return fuelOrder;
    }

}
