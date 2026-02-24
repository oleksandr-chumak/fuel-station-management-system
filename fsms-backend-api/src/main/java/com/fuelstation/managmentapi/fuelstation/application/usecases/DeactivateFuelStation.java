package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationAccessControlChecker;
import com.fuelstation.managmentapi.fuelstation.application.support.FuelStationFetcher;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
@AllArgsConstructor
public class DeactivateFuelStation {

    private final FuelStationFetcher fuelStationFetcher;
    private final FuelStationAccessControlChecker accessControlChecker;
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelStation process(long fuelStationId, Credentials credentials) {
        var fuelStation = fuelStationFetcher.fetchActiveById(fuelStationId);
        accessControlChecker.checkAccess(fuelStation, credentials);

        fuelStation.deactivate();

        fuelStationRepository.save(fuelStation);
        domainEventPublisher.publishAll(fuelStation.getDomainEvents());

        return fuelStation;
    }

}
