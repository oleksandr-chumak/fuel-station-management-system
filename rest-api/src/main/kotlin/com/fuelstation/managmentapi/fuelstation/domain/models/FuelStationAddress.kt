package com.fuelstation.managmentapi.fuelstation.domain.models

data class FuelStationAddress (
    val street: String,
    val buildingNumber: String,
    val city: String,
    val postalCode: String,
    val country: String
)