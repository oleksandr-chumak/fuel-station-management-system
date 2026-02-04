package com.fuelstation.managmentapi.authentication.application;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class AuthTestClientImpl implements AuthTestClient {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResultActions loginManager(AuthRequest authRequest) throws Exception {
        return mockMvc.perform(post("/api/auth/login/manager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
    }

    @Override
    public String loginManagerAndGetToken(AuthRequest authRequest) throws Exception {
        return loginManager(authRequest).andReturn().getResponse().getContentAsString();
    }

    @Override
    public ResultActions loginAdmin(AuthRequest authRequest) throws Exception {
        return mockMvc.perform(post("/api/auth/login/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
    }

    @Override
    public String loginAdminAndGetToken(AuthRequest authRequest) throws Exception {
        return loginAdmin(authRequest).andReturn().getResponse().getContentAsString();
    }

    @Override
    public ResultActions getMe(String token) throws Exception {
        return mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token));
    }

    @Override
    public Me getMeAndReturnResponse(String token) throws Exception {
        return getResponse(getMe(token), Me.class, status().isOk());
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
