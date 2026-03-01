package com.fuelstation.managmentapi.fuelorder.infrastructure.persistence;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.fuelorder.domain.events.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class FuelOrderEventRepositoryImpl implements FuelOrderEventRepository {

    private final JpaFuelOrderEventRepository jpaFuelOrderEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public FuelOrderEvent save(FuelOrderEvent event) {
        Map<String, Object> payload = objectMapper.convertValue(event, new TypeReference<>() {});
        payload.remove("type");
        payload.remove("occurredAt");
        payload.remove("performedBy");
        payload.remove("fuelOrderId");
        payload.remove("fuelStationId");

        var entity = new FuelOrderEventEntity();
        entity.setFuelOrderId(event.getFuelOrderId());
        entity.setFuelStationId(event.getFuelStationId());
        entity.setEventType(event.getType().name());
        entity.setOccurredAt(event.getOccurredAt().atOffset(ZoneOffset.UTC));
        entity.setPerformedBy(event.getPerformedBy().isSystem() ? null : event.getPerformedBy().id());
        entity.setPayload(payload.isEmpty() ? null : payload);

        jpaFuelOrderEventRepository.save(entity);

        return event;
    }

    @Override
    public List<FuelOrderEvent> findByFuelStationIdAfter(Long fuelStationId, Instant occurredAfter, int limit) {
        return jpaFuelOrderEventRepository
                .findByFuelStationIdAndOccurredAt(
                        fuelStationId,
                        occurredAfter.atOffset(ZoneOffset.UTC),
                        PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "occurredAt"))
                )
                .stream()
                .map(this::toFuelOrderEvent)
                .toList();
    }

    @Override
    public List<FuelOrderEvent> findLatestByFuelStationId(Long fuelStationId, int limit) {
        return jpaFuelOrderEventRepository
                .findByFuelStationId(
                        fuelStationId,
                        PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "occurredAt"))
                )
                .stream()
                .map(this::toFuelOrderEvent)
                .toList();
    }

    private FuelOrderEvent toFuelOrderEvent(FuelOrderEventEntity entity) {
        long fuelOrderId = entity.getFuelOrderId();
        long fuelStationId = entity.getFuelStationId();
        Actor performedBy = entity.getPerformedBy() == null ? Actor.system() : Actor.user(entity.getPerformedBy());
        Instant occurredAt = entity.getOccurredAt().toInstant();

        return switch (FuelOrderEventType.valueOf(entity.getEventType())) {
            case FUEL_ORDER_CREATED -> new FuelOrderCreated(fuelOrderId, fuelStationId, performedBy, occurredAt);
            case FUEL_ORDER_CONFIRMED -> new FuelOrderConfirmed(fuelOrderId, fuelStationId, performedBy, occurredAt);
            case FUEL_ORDER_REJECTED -> new FuelOrderRejected(fuelOrderId, fuelStationId, performedBy, occurredAt);
            case FUEL_ORDER_PROCESSED -> new FuelOrderProcessed(fuelOrderId, fuelStationId, performedBy, occurredAt);
        };
    }
}
