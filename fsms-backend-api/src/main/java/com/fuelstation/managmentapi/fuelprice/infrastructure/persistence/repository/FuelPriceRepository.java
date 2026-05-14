package com.fuelstation.managmentapi.fuelprice.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelprice.domain.FuelPrice;

import java.util.List;

public interface FuelPriceRepository {

    List<FuelPrice> findAll();

    List<FuelPrice> findLatest();

}
