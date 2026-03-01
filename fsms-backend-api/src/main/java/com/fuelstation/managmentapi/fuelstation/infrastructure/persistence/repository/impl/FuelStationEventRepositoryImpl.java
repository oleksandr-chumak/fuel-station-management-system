package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.impl;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.events.*;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationEventEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationEventRepository;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa.JpaFuelStationEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class FuelStationEventRepositoryImpl implements FuelStationEventRepository {

    private final JpaFuelStationEventRepository jpaFuelStationEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public FuelStationEvent save(FuelStationEvent event) {
        Map<String, Object> payload = objectMapper.convertValue(event, new TypeReference<>() {});
        payload.remove("type");
        payload.remove("occurredAt");
        payload.remove("performedBy");
        payload.remove("fuelStationId");

        var entity = new FuelStationEventEntity();
        entity.setFuelStationId(event.getFuelStationId());
        entity.setEventType(event.getType().name());
        entity.setOccurredAt(event.getOccurredAt().atOffset(java.time.ZoneOffset.UTC));
        entity.setPerformedBy(event.getPerformedBy().id());
        entity.setPayload(payload.isEmpty() ? null : payload);

        jpaFuelStationEventRepository.save(entity);

        return event;
    }

    @Override
    public List<FuelStationEvent> findByFuelStationIdAfter(Long fuelStationId, Instant occurredAfter, int limit) {
        return jpaFuelStationEventRepository
                .findByFuelStationIdAndOccurredAt(
                        fuelStationId,
                        occurredAfter.atOffset(ZoneOffset.UTC),
                        PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "occurredAt"))
                )
                .stream()
                .map(this::toFuelStationEvent)
                .toList();
    }

    @Override
    public List<FuelStationEvent> findLatestByFuelStationId(Long fuelStationId, int limit) {
        return jpaFuelStationEventRepository
                .findByFuelStationId(fuelStationId, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "occurredAt")))
                .stream()
                .map(this::toFuelStationEvent)
                .toList();
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
