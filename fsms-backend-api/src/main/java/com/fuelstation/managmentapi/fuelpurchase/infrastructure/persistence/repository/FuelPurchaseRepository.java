package com.fuelstation.managmentapi.fuelpurchase.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelpurchase.domain.FuelPurchase;

import java.util.List;

public interface FuelPurchaseRepository {
    FuelPurchase save(FuelPurchase purchase);
    List<FuelPurchase> findByFuelStationId(long fuelStationId);
}
