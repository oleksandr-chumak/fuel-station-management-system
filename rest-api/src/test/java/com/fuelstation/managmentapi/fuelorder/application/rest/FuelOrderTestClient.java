package com.fuelstation.managmentapi.fuelorder.application.rest;

import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

public interface FuelOrderTestClient {
    ResultActions createFuelOrder(CreateFuelOrderRequest request) throws Exception;
    FuelOrderResponse createFuelOrderAndReturnResponse(CreateFuelOrderRequest request) throws Exception;

    ResultActions confirmFuelOrder(long fuelOrderId) throws Exception;
    FuelOrderResponse confirmFuelOrderAndReturnResponse(long fuelOrderId) throws Exception;

    ResultActions rejectFuelOrder(long fuelOrderId) throws Exception;
    FuelOrderResponse rejectFuelOrderAndReturnResponse(long fuelOrderId) throws Exception;

    ResultActions getAllFuelOrders() throws Exception;
    List<FuelOrderResponse> getAllFuelOrderAndReturnResponse() throws Exception;

    ResultActions getFuelOrderById(long fuelOrderId) throws Exception;
    FuelOrderResponse getFuelOrderByIdAndReturnResponse(long fuelOrderId) throws Exception;
}
