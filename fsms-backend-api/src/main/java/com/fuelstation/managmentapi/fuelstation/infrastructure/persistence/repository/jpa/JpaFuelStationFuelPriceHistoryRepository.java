package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa;

import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationFuelPriceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaFuelStationFuelPriceHistoryRepository
        extends JpaRepository<FuelStationFuelPriceHistoryEntity, Long> {

    List<FuelStationFuelPriceHistoryEntity> findByFuelStationIdOrderByChangedAtDesc(Long fuelStationId);

}
