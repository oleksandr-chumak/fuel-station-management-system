package com.fuelstation.managmentapi.fuelstation.application.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.fuelstation.managmentapi.common.AdminUserTest;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.rest.CreateFuelOrderRequest;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderResponse;
import com.fuelstation.managmentapi.fuelorder.application.rest.FuelOrderTestClient;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPriceRequest;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.manager.application.rest.ManagerResponse;
import com.fuelstation.managmentapi.manager.application.rest.ManagerTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class FuelStationControllerTest {

    @Autowired
    private FuelStationTestClient fuelStationTestClient;

    @Autowired
    private FuelOrderTestClient fuelOrderTestClient;

    @Autowired
    private ManagerTestClient managerTestClient;

    private FuelStationResponse testFuelStation;
    private ManagerResponse testManager;

    @BeforeEach
    public void setup() throws Exception {
        testManager = managerTestClient.createManagerAndReturnResponse();
        testFuelStation = fuelStationTestClient.createFuelStationAndReturnResponse();
    }

    @AdminUserTest
    @DisplayName("Should create fuel station")
    public void shouldCreateFuelStation() throws Exception {
        FuelStationResponse fuelStationResponse = fuelStationTestClient.createFuelStationAndReturnResponse();
        assertThat(fuelStationResponse.getBuildingNumber()).isEqualTo("Test");
    }

    @AdminUserTest
    @DisplayName("Should deactivate fuel station")
    public void shouldDeactivateFuelStation() throws Exception {
        FuelStationResponse fuelStationResponse = fuelStationTestClient.deactivateFuelStationAndReturnResponse(testFuelStation.getId());
        assertThat(fuelStationResponse.getStatus()).isEqualTo(FuelStationStatus.DEACTIVATED.toString());
    }

    @AdminUserTest
    @DisplayName("Should assign manager to fuel station")
    public void shouldAssignManagerToFuelStation() throws Exception {
        FuelStationResponse fuelStationResponse = fuelStationTestClient.assignManagerToFuelStationAndReturnResponse(testFuelStation.getId(), testManager.getId());
        assertThat(fuelStationResponse.getAssignedManagersIds()).contains(testManager.getId());
    }

    @AdminUserTest
    @DisplayName("Should unassign manager from fuel station")
    public void shouldUnassignManagerFromFuelStation() throws Exception {
        fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getId(), testManager.getId());
        FuelStationResponse fuelStationResponse = fuelStationTestClient.unassignManagerFromFuelStationAndReturnResponse(testFuelStation.getId(), testManager.getId());
        assertThat(fuelStationResponse.getAssignedManagersIds()).isEmpty();
    }

    @AdminUserTest
    @DisplayName("Should change fuel price")
    public void shouldChangeFuelPrice() throws Exception {
        ChangeFuelPriceRequest changeFuelPriceRequest = new ChangeFuelPriceRequest(FuelGrade.RON_92, 10f);
        FuelStationResponse fuelStationResponse = fuelStationTestClient.changeFuelPriceAndReturnResponse(testFuelStation.getId(), changeFuelPriceRequest);

        Optional<FuelStationResponse.FuelPriceResponse> updatedFuelPrice = fuelStationResponse.getFuelPrices()
                .stream()
                .filter(fuelPriceResponse -> fuelPriceResponse.fuelGrade().equals(FuelGrade.RON_92.toString()))
                .findFirst();

        if (updatedFuelPrice.isPresent()) {
            assertThat(updatedFuelPrice.get().pricePerLiter()).isEqualTo(10f);
        } else {
            assertThat(updatedFuelPrice).isNotNull();
        }
    }

    @AdminUserTest
    @DisplayName("Should get fuel station by id")
    public void shouldGetFuelStationById() throws Exception {
        FuelStationResponse fuelStationResponse = fuelStationTestClient.getFuelStationByIdAndReturnResponse(testFuelStation.getId());
        assertThat(fuelStationResponse.getId()).isEqualTo(testFuelStation.getId());
    }

    @AdminUserTest
    @DisplayName("Should get all fuel stations")
    public void shouldGetAllFuelStations() throws Exception {
        List<?> fuelStationResponses = fuelStationTestClient.getAllFuelStationsAndReturnResponse();
        assertThat(fuelStationResponses.size()).isEqualTo(1);
    }

    @AdminUserTest
    @DisplayName("Should get all managers assigned to the fuel station")
    public void shouldGetAllManagersAssignedToFuelStation() throws Exception {
        fuelStationTestClient.assignManagerToFuelStation(testFuelStation.getId(), testManager.getId());
        List<ManagerResponse> managers = fuelStationTestClient.getManagersAssignedToFuelStationAndReturnResponse(testFuelStation.getId());
        assertThat(managers.size()).isEqualTo(1);
    }

    @AdminUserTest
    @DisplayName("Should get all fuel orders related to the fuel station")
    public void shouldGetAllFuelOrdersRelatedToFuelStation() throws Exception {
        fuelOrderTestClient.createFuelOrder(new CreateFuelOrderRequest(testFuelStation.getId(), FuelGrade.RON_92, 10));
        fuelOrderTestClient.createFuelOrder(new CreateFuelOrderRequest(testFuelStation.getId(), FuelGrade.DIESEL, 10));

        List<FuelOrderResponse> fuelOrders = fuelStationTestClient.getFuelStationFuelOrdersAndReturnResponse(testFuelStation.getId());
        assertThat(fuelOrders.size()).isEqualTo(2);
    }

}