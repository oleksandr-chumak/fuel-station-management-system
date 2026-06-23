package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa;

import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelTankVolumeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaFuelTankVolumeHistoryRepository extends JpaRepository<FuelTankVolumeHistoryEntity, Long> {

    List<FuelTankVolumeHistoryEntity> findByFuelStationIdOrderByChangedAtDesc(Long fuelStationId);

}
