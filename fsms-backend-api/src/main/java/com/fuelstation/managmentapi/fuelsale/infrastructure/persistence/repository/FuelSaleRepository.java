package com.fuelstation.managmentapi.fuelsale.infrastructure.persistence.repository;

import com.fuelstation.managmentapi.fuelsale.domain.FuelSale;

import java.util.List;

public interface FuelSaleRepository {
    FuelSale save(FuelSale sale);
    List<FuelSale> findByFuelStationId(long fuelStationId);
}
