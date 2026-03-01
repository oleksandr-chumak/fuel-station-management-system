package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.impl;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.events.*;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationEventEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationEventRepository;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa.JpaFuelStationEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Repository
@AllArgsConstructor
public class FuelStationEventRepositoryImpl implements FuelStationEventRepository {

    private final JpaFuelStationEventRepository jpaFuelStationEventRepository;

    @Override
    public Page<FuelStationEvent> findByFuelStationId(Long fuelStationId, Pageable pageable) {
        return jpaFuelStationEventRepository
                .findByFuelStationId(fuelStationId, pageable)
                .map(this::toFuelStationEvent);
    }

    @Override
    public Page<FuelStationEvent> findByFuelStationIdAndEventType(Long fuelStationId, FuelStationEventType eventType, Pageable pageable) {
        return jpaFuelStationEventRepository
                .findByFuelStationIdAndEventType(fuelStationId, eventType.name(), pageable)
                .map(this::toFuelStationEvent);
    }

    private FuelStationEvent toFuelStationEvent(FuelStationEventEntity entity) {
        long fuelStationId = entity.getFuelStationId();
        Actor performedBy = entity.getPerformedBy() == null ? Actor.system() : Actor.user(entity.getPerformedBy());
        Instant occurredAt = entity.getOccurredAt().toInstant();
        Map<String, Object> payload = entity.getPayload() != null ? entity.getPayload() : Map.of();

        switch (FuelStationEventType.valueOf(entity.getEventType())) {
            case FUEL_STATION_CREATED:
                return new FuelStationCreated(fuelStationId, performedBy, occurredAt);

            case FUEL_STATION_DEACTIVATED:
                return new FuelStationDeactivated(fuelStationId, performedBy, occurredAt);

            case FUEL_STATION_FUEL_PRICE_CHANGED:
                FuelGrade fuelGrade = FuelGrade.fromString(payload.get("fuelGrade").toString());
                BigDecimal pricePerLiter = new BigDecimal(payload.get("pricePerLiter").toString());
                return new FuelPriceChanged(fuelStationId, fuelGrade, pricePerLiter, performedBy, occurredAt);

            case MANAGER_ASSIGNED_TO_FUEL_STATION:
                long assignedManagerId = ((Number) payload.get("managerId")).longValue();
                return new ManagerAssignedToFuelStation(fuelStationId, assignedManagerId, performedBy, occurredAt);

            case MANAGER_UNASSIGNED_FROM_FUEL_STATION:
                long unassignedManagerId = ((Number) payload.get("managerId")).longValue();
                return new ManagerUnassignedFromFuelStation(fuelStationId, unassignedManagerId, performedBy, occurredAt);

            default:
                throw new IllegalArgumentException("Unknown event type: " + entity.getEventType());
        }
    }
}
