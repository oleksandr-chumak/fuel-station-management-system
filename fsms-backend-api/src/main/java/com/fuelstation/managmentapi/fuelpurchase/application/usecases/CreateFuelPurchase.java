package com.fuelstation.managmentapi.fuelpurchase.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.CurrencyCode;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelorder.application.query.GetFuelOrderByIdQuery;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelpurchase.domain.FuelPurchase;
import com.fuelstation.managmentapi.fuelpurchase.domain.events.FuelPurchaseCreated;
import com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.repository.FuelPurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
@AllArgsConstructor
public class CreateFuelPurchase {

    private final GetFuelOrderByIdQuery getFuelOrderByIdQuery;
    private final FuelPurchaseRepository fuelPurchaseRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelPurchase process(
        long fuelOrderId,
        long fuelStationId,
        BigDecimal pricePerLiter,
        CurrencyCode currency,
        Actor performedBy
    ) {
        FuelOrder order = getFuelOrderByIdQuery.process(fuelOrderId);

        BigDecimal volume = order.getVolume();
        BigDecimal total = volume.multiply(pricePerLiter);

        FuelPurchase purchase = new FuelPurchase(
                null,
                fuelOrderId,
                fuelStationId,
                order.getGrade(),
                volume,
                pricePerLiter,
                currency,
                total,
                OffsetDateTime.now()
        );

        FuelPurchase saved = fuelPurchaseRepository.save(purchase);
        domainEventPublisher.publish(new FuelPurchaseCreated(
                saved.getFuelPurchaseId(), saved.getFuelStationId(), saved.getFuelOrderId(), performedBy));
        return saved;
    }
}
