package com.fuelstation.managmentapi.manager.application.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

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
