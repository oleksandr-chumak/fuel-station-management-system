package com.fuelstation.management_api.fuel-station.domain.models

import java.time.LocalDate

data class FuelStation (
    val id: Long, 
    val address: FuelStationAddress,
    val fuelTanks: List<FuelTank>, 
    val fuelPrices: List<FuelPrice>, 
    val assignedManagersIds: List<Long>,
    val status: FuelStationStatus, 
    val createdAt: LocalDate
)