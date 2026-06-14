package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.country.domain.CountryCode;
import com.fuelstation.managmentapi.common.domain.FuelUnit;
import com.fuelstation.managmentapi.fuelgrade.infrastructure.persistence.repository.FuelGradeRepository;
import com.fuelstation.managmentapi.fuelprice.application.query.ListTaxedFuelPriceQuery;
import com.fuelstation.managmentapi.fuelstation.domain.models.*;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationFuelPriceHistoryEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationFuelPriceHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationCreated;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@AllArgsConstructor
public class CreateFuelStation {

    private static final BigDecimal MARKUP = new BigDecimal("1.10");

    private final FuelGradeRepository fuelGradeRepository;
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final ListTaxedFuelPriceQuery listTaxedFuelPriceQuery;
    private final FuelStationFuelPriceHistoryRepository fuelPriceHistoryRepository;

    @Transactional
    public FuelStation process(
        String street,
        String buildingNumber,
        String city,
        String postalCode,
        CountryCode country,
        Actor performedBy
    ) {
        var availableFuelGrades = fuelGradeRepository.findAvailableByCountry(country);
        var fuelPrices = listTaxedFuelPriceQuery.handle(country, true).stream()
            .filter(
                t -> t.fuelPrice().unit() == FuelUnit.LITER
                    && availableFuelGrades.contains(t.fuelPrice().fuelGrade())
            )
            .map(t -> new FuelStationFuelPrice(
                t.fuelPrice().fuelGrade(),
                t.fuelPrice().price().multiply(MARKUP),
                t.fuelPrice().currency()
            ))
            .toList();
        var fuelTanks = availableFuelGrades.stream().map(FuelTank::create).toList();
        var address = new FuelStationAddress(street, buildingNumber, city, postalCode, country);

        var fuelStation = FuelStation.create(
            address,
            fuelTanks,
            fuelPrices
        );

        var savedFuelStation = fuelStationRepository.save(fuelStation);
        domainEventPublisher.publish(new FuelStationCreated(savedFuelStation.getFuelStationId(), performedBy));

        saveInitialPriceHistory(savedFuelStation, performedBy);

        return savedFuelStation;
    }

    private void saveInitialPriceHistory(FuelStation station, Actor performedBy) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        Long actorId = performedBy.isSystem() ? null : performedBy.id();

        for (FuelStationFuelPrice price : station.getFuelPrices()) {
            var entry = new FuelStationFuelPriceHistoryEntity();
            entry.setFuelStationId(station.getFuelStationId());
            entry.setFuelGrade(price.fuelGrade().toString());
            entry.setPricePerLiter(price.pricePerLiter());
            entry.setCurrency(price.currency().name());
            entry.setChangedAt(now);
            entry.setChangedBy(actorId);
            fuelPriceHistoryRepository.save(entry);
        }
    }

}
