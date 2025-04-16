package com.fuelstation.managmentapi.fuelstation.domain

import com.fuelstation.managmentapi.common.domain.FuelGrade
import org.springframework.stereotype.Service

@Service
class DomainFuelStationService : FuelStationService {

    override fun createFuelStation(street: String, buildinNumber: String, city: String, postalCode: String, country: String) { }

    override fun assignManager(gasStationId: Int, managerId: Int) { }

    override fun unassignManager(gasStationId: Int, managerId: Int) { }

    override fun changeFuelPrice(gasStationId: Int, fuelGrade: FuelGrade, newPrice: Float) { }

    override fun processFuelDelivery(fuelOrderId: Int) { }
}