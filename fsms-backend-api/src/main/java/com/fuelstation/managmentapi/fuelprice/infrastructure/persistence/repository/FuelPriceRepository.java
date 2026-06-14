package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelprice.domain.FuelPrice;

import java.util.List;

public interface FuelPriceRepository {

    List<FuelPrice> findAll();

    List<FuelPrice> findByFuelGrades(List<Long> fuelGradeIds);

    List<FuelPrice> findLatest();

    List<FuelPrice> findLatestByFuelGrades(List<Long> fuelGradeIds);

}
