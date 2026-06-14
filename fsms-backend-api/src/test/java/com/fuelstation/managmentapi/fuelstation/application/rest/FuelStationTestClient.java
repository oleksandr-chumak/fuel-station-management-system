package com.fuelstation.managmentapi.fuelstation.application.rest;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.CreateFuelStationRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPriceRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPricesBulkRequest;
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

public interface FuelStationTestClient {
    ResultActions createFuelStation(CreateFuelStationRequest request) throws Exception;
    FuelStationResponse createFuelStationAndReturnResponse(CreateFuelStationRequest request) throws Exception;
    FuelStationResponse createFuelStationAndReturnResponse() throws Exception;

    ResultActions deactivateFuelStation(long fuelStationId) throws Exception;
    FuelStationResponse deactivateFuelStationAndReturnResponse(long fuelStationId) throws Exception;

    ResultActions assignManagerToFuelStation(long fuelStationId, long managerId) throws Exception;
    ManagerResponse assignManagerToFuelStationAndReturnResponse(long fuelStationId, long managerId) throws Exception;

    ResultActions unassignManagerFromFuelStation(long fuelStationId, long managerId) throws Exception;
    ManagerResponse unassignManagerFromFuelStationAndReturnResponse(long fuelStationId, long managerId) throws Exception;

    ResultActions updateFuelPrice(long fuelStationId, FuelGrade fuelGrade, ChangeFuelPriceRequest request) throws Exception;
    ResultActions updateFuelPriceRaw(long fuelStationId, String fuelGrade, ChangeFuelPriceRequest request) throws Exception;
    FuelStationResponse updateFuelPriceAndReturnResponse(long fuelStationId, FuelGrade fuelGrade, ChangeFuelPriceRequest request) throws Exception;

    ResultActions updateFuelPrices(long fuelStationId, ChangeFuelPricesBulkRequest request) throws Exception;
    FuelStationResponse updateFuelPricesAndReturnResponse(long fuelStationId, ChangeFuelPricesBulkRequest request) throws Exception;

    ResultActions getFuelStationById(long fuelStationId) throws Exception;
    FuelStationResponse getFuelStationByIdAndReturnResponse(long fuelStationId) throws Exception;

    ResultActions getAllFuelStations() throws Exception;
    List<FuelStationResponse> getAllFuelStationsAndReturnResponse() throws Exception;

    ResultActions getManagersAssignedToFuelStation(long fuelStationId) throws Exception;
    List<ManagerResponse> getManagersAssignedToFuelStationAndReturnResponse(long fuelStationId) throws Exception;

    ResultActions getFuelStationFuelOrders(long fuelStationId) throws Exception;
    List<FuelOrderResponse> getFuelStationFuelOrdersAndReturnResponse(long fuelStationId) throws Exception;
}
