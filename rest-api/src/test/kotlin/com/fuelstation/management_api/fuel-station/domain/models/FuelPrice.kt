package com.fuelstation.management_api.fuel

import com.fuelstation.management_api.common.domain.FuelGrade

data class FuelPrice(
    val fuelGrade: FuelGrade,
    val pricePerLiter: Float
)