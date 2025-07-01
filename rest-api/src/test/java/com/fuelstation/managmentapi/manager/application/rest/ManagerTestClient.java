package com.fuelstation.managmentapi.manager.application.rest;

import org.springframework.test.web.servlet.ResultActions;

public interface ManagerTestClient {
    ResultActions createManager(CreateManagerRequest createManagerRequest) throws Exception;
    ManagerResponse createManagerAndReturnResponse(CreateManagerRequest createManagerRequest) throws Exception;
    ManagerResponse createManagerAndReturnResponse() throws Exception;
}
