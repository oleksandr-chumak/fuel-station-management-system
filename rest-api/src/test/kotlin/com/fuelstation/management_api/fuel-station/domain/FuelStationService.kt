package com.fuelstation.managment_api.fuel

import com.fuelstation.management_api.common.domain.FuelGrade-station.domain

interface FuelStationService {
    fun createFuelStation(street: String, buildinNumber: String, city: String, postalCode: String, country: String)
    fun assignManager(gasStationId: Int, managerId: Int)
    fun unassignManager(gasStationId: Int, managerId: Int)
    fun changeFuelPrice(gasStationId: Int, fuelGrade: FuelGrade, newPrice: Float)
    fun processFuelDelivery(fuelOrderId: Int)
}