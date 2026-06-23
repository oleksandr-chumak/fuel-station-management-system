package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.application.query.GetActiveFuelStationByIdQuery;
import com.fuelstation.managmentapi.fuelstation.domain.events.FuelTankInstalled;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTank;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InstallFuelTank {

    private final GetActiveFuelStationByIdQuery getActiveFuelStationByIdQuery;
    private final FuelStationRepository fuelStationRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public FuelStation process(long fuelStationId, FuelGrade fuelGrade, BigDecimal maxCapacity, Actor performedBy) {
        FuelStation fuelStation = getActiveFuelStationByIdQuery.process(fuelStationId, performedBy);

        Set<Long> previousTankIds = fuelStation.getFuelTanks().stream()
                .map(FuelTank::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        fuelStation.installFuelTank(fuelGrade, maxCapacity, performedBy);

        FuelStation saved = fuelStationRepository.save(fuelStation);

        FuelTank installedTank = saved.getFuelTanks().stream()
                .filter(t -> t.getId() != null && !previousTankIds.contains(t.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Installed fuel tank was not found after save"));

        domainEventPublisher.publish(new FuelTankInstalled(
                saved.getFuelStationId(),
                installedTank.getId(),
                installedTank.getFuelGrade(),
                installedTank.getMaxCapacity(),
                performedBy
        ));

        return saved;
    }
}
