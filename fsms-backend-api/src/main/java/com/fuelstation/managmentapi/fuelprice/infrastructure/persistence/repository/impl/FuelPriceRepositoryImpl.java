package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.impl;

import com.fuelstation.managmentapi.fuelprice.domain.FuelPrice;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.mapper.FuelPriceMapper;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.FuelPriceRepository;
import com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository.jpa.JpaFuelPriceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class FuelPriceRepositoryImpl implements FuelPriceRepository {

    private final JpaFuelPriceRepository jpaFuelPriceRepository;
    private final FuelPriceMapper fuelPriceMapper;

    @Override
    public List<FuelPrice> findAll() {
        return jpaFuelPriceRepository.findAll().stream().map(fuelPriceMapper::toDomain).toList();
    }

    @Override
    public List<FuelPrice> findLatest() {
        return jpaFuelPriceRepository.findLatest().stream().map(fuelPriceMapper::toDomain).toList();
    }
}
