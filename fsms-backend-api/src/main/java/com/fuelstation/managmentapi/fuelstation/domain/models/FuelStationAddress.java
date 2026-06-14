package com.fuelstation.managmentapi.fuelstation.domain.models;

import com.fuelstation.managmentapi.country.domain.CountryCode;

public record FuelStationAddress(
    String street,
    String buildingNumber,
    String city,
    String postalCode,
    CountryCode country
) {}