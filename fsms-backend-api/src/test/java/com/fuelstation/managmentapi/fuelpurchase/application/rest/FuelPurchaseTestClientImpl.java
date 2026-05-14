package com.fuelstation.managmentapi.fuelpurchase.application.rest;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class FuelPurchaseTestClientImpl implements FuelPurchaseTestClient {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResultActions getPurchasesByStation(long stationId) throws Exception {
        return this.mockMvc.perform(get("/api/fuel-stations/{stationId}/fuel-purchases", stationId));
    }

    @Override
    public List<FuelPurchaseResponse> getPurchasesByStationAndReturnResponse(long stationId) throws Exception {
        return getResponse(getPurchasesByStation(stationId), new TypeReference<>() {}, status().isOk());
    }

    private <T> T getResponse(ResultActions resultActions, TypeReference<T> responseType, ResultMatcher status) throws Exception {
        String responseBody = resultActions.andExpect(status).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, responseType);
    }
}
