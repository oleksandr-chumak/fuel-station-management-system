package com.fuelstation.managmentapi.fuelstation.domain;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;

public interface FuelStationFactory {
    public FuelStation create(String street, String buildingNumber, String city, String postalCode, String country); 
}
