package com.fuelstation.managmentapi.fuelorder.application.rest;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class FuelOrderTestClientImpl implements FuelOrderTestClient {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResultActions createFuelOrder(CreateFuelOrderRequest request) throws Exception {
        return this.mockMvc.perform(post("/api/fuel-orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Override
    public FuelOrderResponse createFuelOrderAndReturnResponse(CreateFuelOrderRequest request) throws Exception {
        return getResponse(createFuelOrder(request), FuelOrderResponse.class, status().isCreated());
    }

    @Override
    public ResultActions confirmFuelOrder(long fuelOrderId) throws Exception {
        return this.mockMvc.perform(put("/api/fuel-orders/{id}/confirm", fuelOrderId));
    }

    @Override
    public FuelOrderResponse confirmFuelOrderAndReturnResponse(long fuelOrderId) throws Exception {
        return getResponse(confirmFuelOrder(fuelOrderId), FuelOrderResponse.class, status().isOk());
    }

    @Override
    public ResultActions rejectFuelOrder(long fuelOrderId) throws Exception {
        return this.mockMvc.perform(put("/api/fuel-orders/{id}/reject", fuelOrderId));
    }

    @Override
    public FuelOrderResponse rejectFuelOrderAndReturnResponse(long fuelOrderId) throws Exception {
        return getResponse(rejectFuelOrder(fuelOrderId), FuelOrderResponse.class, status().isOk());
    }

    @Override
    public ResultActions getAllFuelOrders() throws Exception {
        return this.mockMvc.perform(get("/api/fuel-orders/"));
    }

    @Override
    public List<FuelOrderResponse> getAllFuelOrderAndReturnResponse() throws Exception {
        return getResponse(getAllFuelOrders(), new TypeReference<>() {}, status().isOk());
    }

    @Override
    public ResultActions getFuelOrderById(long fuelOrderId) throws Exception {
        return this.mockMvc.perform(get("/api/fuel-orders/{id}", fuelOrderId));
    }

    @Override
    public FuelOrderResponse getFuelOrderByIdAndReturnResponse(long fuelOrderId) throws Exception {
        return getResponse(getFuelOrderById(fuelOrderId), FuelOrderResponse.class, status().isOk());
    }

    // TODO make it reusable
    private <T> T getResponse(ResultActions resultActions, Class<T> responseType, ResultMatcher status) throws Exception {
        String responseBody = resultActions.andExpect(status).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, responseType);
    }

    // TODO make it reusable
    private <T> T getResponse(ResultActions resultActions, TypeReference<T> responseType, ResultMatcher status) throws Exception {
        String responseBody = resultActions.andExpect(status).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, responseType);
    }
}
