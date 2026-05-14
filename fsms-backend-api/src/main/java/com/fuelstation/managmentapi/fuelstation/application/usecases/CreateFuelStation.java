package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.common.domain.FuelUnit;
import com.fuelstation.managmentapi.fuelprice.application.query.ListTaxedFuelPriceQuery;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationFuelPrice;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.FuelStationFactory;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class CreateFuelStation {

    private static final BigDecimal MARKUP = new BigDecimal("1.10");

    private final FuelStationRepository fuelStationRepository;
    private final FuelStationFactory fuelStationFactory;
    private final DomainEventPublisher domainEventPublisher;
    private final ListTaxedFuelPriceQuery listTaxedFuelPriceQuery;

    @Transactional
    public FuelStation process(
            String street,
            String buildingNumber,
            String city,
            String postalCode,
            CountryCode country,
            Actor performedBy
    ) {
        List<FuelStationFuelPrice> initialPrices = listTaxedFuelPriceQuery.handle(country, true).stream()
                .filter(t -> t.fuelPrice().unit() == FuelUnit.LITER)
                .map(t -> new FuelStationFuelPrice(
                        t.fuelPrice().fuelGrade(),
                        t.fuelPrice().price().multiply(MARKUP),
                        t.fuelPrice().currency()
                ))
                .toList();

        var fuelStation = fuelStationFactory.create(street, buildingNumber, city, postalCode, country, initialPrices);

        var savedFuelStation = fuelStationRepository.save(fuelStation);
        domainEventPublisher.publish(new FuelStationCreated(savedFuelStation.getFuelStationId(), performedBy));

        return savedFuelStation;
    }

}
