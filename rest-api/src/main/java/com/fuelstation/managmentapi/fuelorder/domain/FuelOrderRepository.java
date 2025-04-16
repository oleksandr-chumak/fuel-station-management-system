package com.fuelstation.managmentapi.fuelorder.domain;

import java.util.Optional;

public interface FuelOrderRepository {
    public FuelOrder save(FuelOrder fuelOrder);
    public Optional<FuelOrder> findById(long id);
}
