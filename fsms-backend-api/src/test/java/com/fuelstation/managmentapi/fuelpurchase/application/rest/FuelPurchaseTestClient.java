package com.fuelstation.managmentapi.fuelpurchase.application.rest;

import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

public interface FuelPurchaseTestClient {
    ResultActions getPurchasesByStation(long stationId) throws Exception;
    List<FuelPurchaseResponse> getPurchasesByStationAndReturnResponse(long stationId) throws Exception;
}
