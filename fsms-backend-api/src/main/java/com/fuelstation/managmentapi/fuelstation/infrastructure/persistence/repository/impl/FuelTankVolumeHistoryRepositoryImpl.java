package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.impl;

import com.fuelstation.managmentapi.common.domain.Actor;
import com.fuelstation.managmentapi.common.domain.ActorType;
import com.fuelstation.managmentapi.fuelgrade.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTankVolumeChangeReason;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelTankVolumeHistory;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelTankVolumeHistoryEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelTankVolumeHistoryRepository;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa.JpaFuelTankVolumeHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.List;

@Repository
@AllArgsConstructor
public class FuelTankVolumeHistoryRepositoryImpl implements FuelTankVolumeHistoryRepository {

    private final JpaFuelTankVolumeHistoryRepository jpaRepository;

    @Override
    public FuelTankVolumeHistory save(FuelTankVolumeHistory history) {
        FuelTankVolumeHistoryEntity saved = jpaRepository.save(toEntity(history));
        return toDomain(saved);
    }

    @Override
    public List<FuelTankVolumeHistory> findByFuelStationId(long fuelStationId) {
        return jpaRepository.findByFuelStationIdOrderByChangedAtDesc(fuelStationId).stream()
                .map(this::toDomain)
                .toList();
    }

    private FuelTankVolumeHistoryEntity toEntity(FuelTankVolumeHistory domain) {
        var entity = new FuelTankVolumeHistoryEntity();
        entity.setHistoryId(domain.historyId());
        entity.setFuelStationId(domain.fuelStationId());
        entity.setFuelTankId(domain.fuelTankId());
        entity.setFuelGrade(domain.fuelGrade().toString());
        entity.setOldVolume(domain.oldVolume());
        entity.setNewVolume(domain.newVolume());
        entity.setReason(domain.reason().name());
        entity.setChangedAt(domain.changedAt().atOffset(ZoneOffset.UTC));
        entity.setChangedBy(domain.performedBy().isSystem() ? null : domain.performedBy().id());
        return entity;
    }

    private FuelTankVolumeHistory toDomain(FuelTankVolumeHistoryEntity entity) {
        Actor performedBy = entity.getChangedBy() == null
                ? Actor.system()
                : new Actor(entity.getChangedBy(), ActorType.USER, null);
        return new FuelTankVolumeHistory(
                entity.getHistoryId(),
                entity.getFuelStationId(),
                entity.getFuelTankId(),
                FuelGrade.fromString(entity.getFuelGrade()),
                entity.getOldVolume(),
                entity.getNewVolume(),
                FuelTankVolumeChangeReason.valueOf(entity.getReason()),
                performedBy,
                entity.getChangedAt().toInstant()
        );
    }
}
