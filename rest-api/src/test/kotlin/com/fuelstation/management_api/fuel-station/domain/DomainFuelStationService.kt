package com.fuelstation.management_api.fuel-station.domain

import org.springframework.stereotype.Service
import com.fuelstation.managment_api.fuel.FuelStationService
import com.fuelstation.management_api.common.domain.FuelGrade

@Service
class DomainFuelStationService : FuelStationService {

    override fun createFuelStation(street: String, buildinNumber: String, city: String, postalCode: String, country: String) { }

    override fun assignManager(gasStationId: Int, managerId: Int) { }

    override fun unassignManager(gasStationId: Int, managerId: Int) { }

    override fun changeFuelPrice(gasStationId: Int, fuelGrade: FuelGrade, newPrice: Float) { }

    override fun processFuelDelivery(fuelOrderId: Int) { }
}