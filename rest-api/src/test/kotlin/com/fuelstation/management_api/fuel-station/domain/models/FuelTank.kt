package com.fuelstation.management_api.fuel

import com.fuelstation.management_api.common.domain.FuelGrade
import java.time.LocalDate-station.domain.models

data class FuelTank(
    val id: Number, 
    val fuelGrade: FuelGrade,
    val currentVolume: Float,
    val maxCapacity: Int,
    val lastRefilDate: LocalDate
)