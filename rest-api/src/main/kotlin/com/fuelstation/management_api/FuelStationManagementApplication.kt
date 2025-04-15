package com.fuelstation.management_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FuelStationManagementApplication

fun main(args: Array<String>) {
	runApplication<FuelStationManagementApplication>(*args)
}
