package com.fuelstation.managmentapi.fuelstation.application.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderResponse;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.AssignManagerRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPriceRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.CreateFuelStationRequest;
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;
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
public class FuelStationTestClientImpl implements FuelStationTestClient {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public ResultActions createFuelStation(CreateFuelStationRequest request) throws Exception {
         return this.mockMvc.perform(post("/api/fuel-stations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    public FuelStationResponse createFuelStationAndReturnResponse(CreateFuelStationRequest request) throws Exception {
        return getResponse(createFuelStation(request), FuelStationResponse.class, status().isCreated());
    }

    public FuelStationResponse createFuelStationAndReturnResponse() throws Exception {
        return this.createFuelStationAndReturnResponse(new CreateFuelStationRequest(
                "Test",
                "Test",
                "Test",
                "12345",
                "Test"
        ));
    }

    public ResultActions deactivateFuelStation(long fuelStationId) throws Exception {
        return this.mockMvc.perform(put("/api/fuel-stations/{id}/deactivate", fuelStationId));
    }

    public FuelStationResponse deactivateFuelStationAndReturnResponse(long fuelStationId) throws Exception {
        return getResponse(deactivateFuelStation(fuelStationId), FuelStationResponse.class, status().isOk());
    }

    public ResultActions assignManagerToFuelStation(long fuelStationId, long managerId) throws Exception {
        return this.mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", fuelStationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AssignManagerRequest(managerId))));
    }

    public FuelStationResponse assignManagerToFuelStationAndReturnResponse(long fuelStationId, long managerId) throws Exception {
        return getResponse(assignManagerToFuelStation(fuelStationId, managerId), FuelStationResponse.class, status().isOk());
    }

    @Override
    public ResultActions unassignManagerFromFuelStation(long fuelStationId, long managerId) throws Exception {
        return this.mockMvc.perform(put("/api/fuel-stations/{id}/unassign-manager", fuelStationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AssignManagerRequest(managerId))));
    }

    @Override
    public FuelStationResponse unassignManagerFromFuelStationAndReturnResponse(long fuelStationId, long managerId) throws Exception {
        return getResponse(unassignManagerFromFuelStation(fuelStationId, managerId), FuelStationResponse.class, status().isOk());
    }

    @Override
    public ResultActions changeFuelPrice(long fuelStationId, ChangeFuelPriceRequest request) throws Exception {
        return this.mockMvc.perform(put("/api/fuel-stations/{id}/change-fuel-price", fuelStationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Override
    public FuelStationResponse changeFuelPriceAndReturnResponse(long fuelStationId, ChangeFuelPriceRequest request) throws Exception {
        return getResponse(changeFuelPrice(fuelStationId, request), FuelStationResponse.class, status().isOk());
    }

    @Override
    public ResultActions getFuelStationById(long fuelStationId) throws Exception {
        return this.mockMvc.perform(get("/api/fuel-stations/{id}", fuelStationId));
    }

    @Override
    public FuelStationResponse getFuelStationByIdAndReturnResponse(long fuelStationId) throws Exception {
        return getResponse(getFuelStationById(fuelStationId), FuelStationResponse.class, status().isOk());
    }

    @Override
    public ResultActions getAllFuelStations() throws Exception {
        return this.mockMvc.perform(get("/api/fuel-stations/"));
    }

    @Override
    public List<FuelStationResponse> getAllFuelStationsAndReturnResponse() throws Exception {
        return getResponse(getAllFuelStations(), new TypeReference<>() {}, status().isOk());
    }

    @Override
    public ResultActions getManagersAssignedToFuelStation(long fuelStationId) throws Exception {
        return this.mockMvc.perform(get("/api/fuel-stations/{id}/managers", fuelStationId));
    }

    @Override
    public List<ManagerResponse> getManagersAssignedToFuelStationAndReturnResponse(long fuelStationId) throws Exception {
        return getResponse(getManagersAssignedToFuelStation(fuelStationId), new TypeReference<>() {}, status().isOk());
    }

    @Override
    public ResultActions getFuelStationFuelOrders(long fuelStationId) throws Exception {
        return this.mockMvc.perform(get("/api/fuel-stations/{id}/fuel-orders", fuelStationId));
    }

    @Override
    public List<FuelOrderResponse> getFuelStationFuelOrdersAndReturnResponse(long fuelStationId) throws Exception {
        return getResponse(getFuelStationFuelOrders(fuelStationId), new TypeReference<>() {}, status().isOk());
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
