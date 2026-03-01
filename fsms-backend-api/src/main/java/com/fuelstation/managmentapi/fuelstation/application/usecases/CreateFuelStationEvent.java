package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.fuelstation.domain.events.FuelStationEvent;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationEventEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa.JpaFuelStationEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
@AllArgsConstructor
public class CreateFuelStationEvent {

    private final JpaFuelStationEventRepository repository;
    private final ObjectMapper objectMapper;

    public FuelStationEventEntity process(FuelStationEvent event) {
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

        return repository.save(entity);
    }

}
