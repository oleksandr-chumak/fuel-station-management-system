package com.fuelstation.managmentapi.fuelorder.application.rest;

import org.springframework.test.web.servlet.ResultActions;

public interface FuelOrderTestClient {
    ResultActions createFuelOrder(CreateFuelOrderRequest request) throws Exception;
    FuelOrderResponse createFuelOrderAndReturnResponse(CreateFuelOrderRequest request) throws Exception;
}
