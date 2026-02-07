package com.fuelstation.managmentapi.manager.application.rest;

import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationResponse;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

public interface ManagerTestClient {
    ResultActions createManager(CreateManagerRequest createManagerRequest) throws Exception;
    ManagerResponse createManagerAndReturnResponse(CreateManagerRequest createManagerRequest) throws Exception;
    ManagerResponse createManagerAndReturnResponse() throws Exception;

    ResultActions terminateManager(long managerId) throws Exception;
    ManagerResponse terminateManagerAndReturnResponse(long managerId) throws Exception;

    ResultActions getAllManagers() throws Exception;
    List<ManagerResponse> getAllManagersAndReturnResponse() throws Exception;

    ResultActions getManagerById(long managerId) throws Exception;
    ManagerResponse getManagerByIdAndReturnResponse(long managerId) throws Exception;

    ResultActions getManagerFuelStations(long managerId) throws Exception;
    List<FuelStationResponse> getManagerFuelStationsAndReturnResponse(long managerId) throws Exception;
}
