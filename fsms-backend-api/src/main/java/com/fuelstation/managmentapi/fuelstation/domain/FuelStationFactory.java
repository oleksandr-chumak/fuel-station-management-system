package com.fuelstation.managmentapi.fuelstation.domain;

import com.fuelstation.managmentapi.common.domain.CountryCode;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationFuelPrice;

import java.util.List;

public interface FuelStationFactory {
    FuelStation create(String street, String buildingNumber, String city, String postalCode, CountryCode country, List<FuelStationFuelPrice> initialPrices);
}
