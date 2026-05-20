package com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.impl;

import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.entity.FuelStationFuelPriceHistoryEntity;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.FuelStationFuelPriceHistoryRepository;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.repository.jpa.JpaFuelStationFuelPriceHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class FuelStationFuelPriceHistoryRepositoryImpl implements FuelStationFuelPriceHistoryRepository {

    private final JpaFuelStationFuelPriceHistoryRepository jpaRepository;

    @Override
    public void save(FuelStationFuelPriceHistoryEntity entry) {
        jpaRepository.save(entry);
    }

    @Override
    public List<FuelStationFuelPriceHistoryEntity> findByFuelStationId(long fuelStationId) {
        return jpaRepository.findByFuelStationIdOrderByChangedAtDesc(fuelStationId);
    }

}
