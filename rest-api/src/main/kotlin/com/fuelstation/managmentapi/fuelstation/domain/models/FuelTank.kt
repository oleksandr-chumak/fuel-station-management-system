package com.fuelstation.managmentapi.fuelstation.domain.models

import com.fuelstation.managmentapi.common.domain.FuelGrade
import java.time.LocalDate


data class FuelTank(
    val id: Long, 
    val fuelGrade: FuelGrade,
    val currentVolume: Float,
    val maxCapacity: Int,
    val lastRefilDate: LocalDate
)