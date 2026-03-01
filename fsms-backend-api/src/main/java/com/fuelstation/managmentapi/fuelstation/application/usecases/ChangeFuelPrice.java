package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.application.UserFetcher;
import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationAccessControlChecker;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationFetcher;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class ChangeFuelPrice {

    private final FuelStationFetcher fuelStationFetcher;
    private final FuelStationAccessControlChecker accessControlChecker;
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final UserFetcher userFetcher;

    @Transactional
    public FuelStation process(long fuelStationId, FuelGrade fuelGrade, BigDecimal newPrice, Actor performedBy) {
        var fuelStation = fuelStationFetcher.fetchActiveById(fuelStationId);
        var credentials = userFetcher.fetchById(performedBy.id());
        accessControlChecker.checkAccess(fuelStation, credentials);

        fuelStation.changeFuelPrice(fuelGrade, newPrice, performedBy);

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }

}
