package com.fuelstation.managmentapi.fuelstation.domain.models

import java.time.LocalDate
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationAddress

data class FuelStation (
    val id: Long, 
    val address: FuelStationAddress,
    val fuelTanks: List<FuelTank>, 
    val fuelPrices: List<FuelPrice>, 
    val assignedManagersIds: List<Long>,
    val status: FuelStationStatus, 
    val createdAt: LocalDate
)