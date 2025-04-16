package com.fuelstation.managmentapi.fuelstation.domain.models

import com.fuelstation.managmentapi.common.domain.FuelGrade

data class FuelPrice(
    val fuelGrade: FuelGrade,
    val pricePerLiter: Float
)