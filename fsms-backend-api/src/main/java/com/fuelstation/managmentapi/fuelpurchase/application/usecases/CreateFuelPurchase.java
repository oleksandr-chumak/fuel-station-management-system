package com.fuelstation.managmentapi.fuelpurchase.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelUnit;
import com.fuelstation.managmentapi.fuelorder.application.query.GetFuelOrderByIdQuery;
import com.fuelstation.managmentapi.fuelorder.domain.FuelOrder;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import com.fuelstation.managmentapi.fuelprice.application.query.ListTaxedFuelPriceQuery;
import com.fuelstation.managmentapi.fuelprice.application.query.model.TaxedFuelPriceResponse;
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
    private final FuelStationRepository fuelStationRepository;
    private final ListTaxedFuelPriceQuery listTaxedFuelPriceQuery;
    private final FuelPurchaseRepository fuelPurchaseRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelPurchase process(long fuelOrderId, long fuelStationId, Actor performedBy) {
        FuelOrder order = getFuelOrderByIdQuery.process(fuelOrderId);

        var station = fuelStationRepository.findById(fuelStationId)
                .orElseThrow(() -> new IllegalStateException("Fuel station not found: " + fuelStationId));

        TaxedFuelPriceResponse taxedPrice = listTaxedFuelPriceQuery
                .handle(station.getAddress().country(), true)
                .stream()
                .filter(t -> t.fuelPrice().fuelGrade() == order.getGrade()
                        && t.fuelPrice().unit() == FuelUnit.LITER)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No taxed price found for grade " + order.getGrade()
                        + " in country " + station.getAddress().country()));

        BigDecimal pricePerLiter = taxedPrice.fuelPrice().price();
        BigDecimal total = order.getVolume().multiply(pricePerLiter);

        FuelPurchase purchase = new FuelPurchase(
                null,
                fuelOrderId,
                fuelStationId,
                order.getGrade(),
                order.getVolume(),
                pricePerLiter,
                taxedPrice.fuelPrice().currency(),
                total,
                OffsetDateTime.now()
        );

        FuelPurchase saved = fuelPurchaseRepository.save(purchase);
        domainEventPublisher.publish(new FuelPurchaseCreated(
                saved.getFuelPurchaseId(), saved.getFuelStationId(), saved.getFuelOrderId(), performedBy));
        return saved;
    }
}
