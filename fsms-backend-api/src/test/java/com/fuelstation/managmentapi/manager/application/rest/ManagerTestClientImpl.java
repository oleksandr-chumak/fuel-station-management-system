package com.fuelstation.managmentapi.manager.application.rest;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationResponse;
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
public class ManagerTestClientImpl implements ManagerTestClient {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResultActions createManager(CreateManagerRequest createManagerRequest) throws Exception {
        return this.mockMvc.perform(post("/api/managers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createManagerRequest)));
    }

    @Override
    public ManagerResponse createManagerAndReturnResponse(CreateManagerRequest createManagerRequest) throws Exception {
        return getResponse(createManager(createManagerRequest), ManagerResponse.class, status().isCreated());
    }

    @Override
    public ManagerResponse createManagerAndReturnResponse() throws Exception {
        return createManagerAndReturnResponse(new CreateManagerRequest("Test", "Test", "manager-test@test.com"));
    }

    @Override
    public ResultActions terminateManager(long managerId) throws Exception {
        return this.mockMvc.perform(put("/api/managers/{id}/terminate", managerId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Override
    public ManagerResponse terminateManagerAndReturnResponse(long managerId) throws Exception {
        return getResponse(terminateManager(managerId), ManagerResponse.class, status().isOk());
    }

    @Override
    public ResultActions getAllManagers() throws Exception {
        return this.mockMvc.perform(get("/api/managers/"));
    }

    @Override
    public List<ManagerResponse> getAllManagersAndReturnResponse() throws Exception {
        return getResponse(getAllManagers(), new TypeReference<>() {}, status().isOk());
    }

    @Override
    public ResultActions getManagerById(long managerId) throws Exception {
        return this.mockMvc.perform(get("/api/managers/{id}", managerId));
    }

    @Override
    public ManagerResponse getManagerByIdAndReturnResponse(long managerId) throws Exception {
        return getResponse(getManagerById(managerId), ManagerResponse.class, status().isOk());
    }

    @Override
    public ResultActions getManagerFuelStations(long managerId) throws Exception {
        return this.mockMvc.perform(get("/api/managers/{id}/fuel-stations", managerId));
    }

    @Override
    public List<FuelStationResponse> getManagerFuelStationsAndReturnResponse(long managerId) throws Exception {
        return getResponse(getManagerFuelStations(managerId), new TypeReference<>() {}, status().isOk());
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
