package com.fuelstation.management_api.fuel

data class FuelStationAddress (
    val street: String,
    val buildingNumber: String,
    val city: String,
    val postalCode: String,
    val country: String
)