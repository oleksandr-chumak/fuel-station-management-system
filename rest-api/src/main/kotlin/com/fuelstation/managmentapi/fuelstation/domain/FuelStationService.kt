package com.fuelstation.managmentapi.fuelstation.domain

import com.fuelstation.managmentapi.common.domain.FuelGrade

interface FuelStationService {
    fun createFuelStation(street: String, buildinNumber: String, city: String, postalCode: String, country: String)
    fun assignManager(gasStationId: Int, managerId: Int)
    fun unassignManager(gasStationId: Int, managerId: Int)
    fun changeFuelPrice(gasStationId: Int, fuelGrade: FuelGrade, newPrice: Float)
    fun processFuelDelivery(fuelOrderId: Int)
}