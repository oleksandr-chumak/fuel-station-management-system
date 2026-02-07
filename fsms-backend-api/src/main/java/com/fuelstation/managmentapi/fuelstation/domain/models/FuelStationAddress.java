package com.fuelstation.managmentapi.fuelstation.domain.models;

public record FuelStationAddress(
    String street,
    String buildingNumber,
    String city,
    String postalCode,
    String country
) {}