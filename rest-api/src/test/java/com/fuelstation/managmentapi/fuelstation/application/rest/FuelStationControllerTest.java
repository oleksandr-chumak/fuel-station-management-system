package com.fuelstation.managmentapi.fuelstation.application.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelorder.application.rest.CreateFuelOrderRequest;
import com.fuelstation.managmentapi.fuelstation.application.common.WithMockAdminUser;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.AssignManagerRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.ChangeFuelPriceRequest;
import com.fuelstation.managmentapi.fuelstation.application.rest.requests.CreateFuelStationRequest;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuelstation.managmentapi.fuelstation.application.usecases.CreateFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.application.usecases.CreateManager;
import com.fuelstation.managmentapi.manager.domain.Manager;

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
    private MockMvc mockMvc;

    @Autowired
    private CreateFuelStation createFuelStation;

    @Autowired
    private CreateManager createManager;

    @Autowired
    private ObjectMapper objectMapper;

    private FuelStation testFuelStation;
    private Manager testManager;

    @BeforeEach
    public void setup() {
        String managerEmail = "test-manager@test.com";
        String password = "1234";

        testManager = createManager.process("Test", "Manager", managerEmail, password);

        testFuelStation = createFuelStation.process(
                "Test Street",
                "123",
                "Test City",
                "12345",
                "Test Country"
        );
    }

    @Test()
    @DisplayName("Should create fuel station")
    @WithMockAdminUser
    public void shouldCreateFuelStation() throws Exception {
        CreateFuelStationRequest request = new CreateFuelStationRequest("Test", "Test", "Test", "Test", "Test");
        String response = this.mockMvc
                .perform(post("/api/fuel-stations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FuelStationResponse fuelStationResponse = objectMapper.readValue(response, FuelStationResponse.class);
        assertThat(fuelStationResponse.getBuildingNumber()).isEqualTo("Test");
    }

    @Test()
    @DisplayName("Should deactivate fuel station")
    @WithMockAdminUser
    public void shouldDeactivateFuelStation() throws Exception {
        String response = this.mockMvc.perform(put("/api/fuel-stations/{id}/deactivate", testFuelStation.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FuelStationResponse fuelStationResponse = objectMapper.readValue(response, FuelStationResponse.class);
        assertThat(fuelStationResponse.getStatus()).isEqualTo(FuelStationStatus.DEACTIVATED.toString());
    }

    @Test()
    @DisplayName("Should assign manager to fuel station")
    @WithMockAdminUser
    public void shouldAssignManagerToFuelStation() throws Exception {
        AssignManagerRequest assignManagerRequest = new AssignManagerRequest(testManager.getId());
        String response = this.mockMvc
                .perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignManagerRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FuelStationResponse fuelStationResponse = objectMapper.readValue(response, FuelStationResponse.class);
        assertThat(fuelStationResponse.getAssignedManagersIds()).contains(testManager.getId());
    }

    @Test()
    @DisplayName("Should unassign manager from fuel station")
    @WithMockAdminUser
    public void shouldUnassignManagerFromFuelStation() throws Exception {
        AssignManagerRequest assignManagerRequest = new AssignManagerRequest(testManager.getId());

        this.mockMvc
                .perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignManagerRequest)))
                .andExpect(status().isOk());

        String response = this.mockMvc
                .perform(put("/api/fuel-stations/{id}/unassign-manager", testFuelStation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignManagerRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FuelStationResponse fuelStationResponse = objectMapper.readValue(response, FuelStationResponse.class);
        assertThat(fuelStationResponse.getAssignedManagersIds()).isEmpty();
    }

    @Test()
    @DisplayName("Should change fuel price")
    @WithMockAdminUser
    public void shouldChangeFuelPrice() throws Exception {
        ChangeFuelPriceRequest changeFuelPriceRequest = new ChangeFuelPriceRequest(FuelGrade.RON_92, 10f);

        String response = this.mockMvc
                .perform(put("/api/fuel-stations/{id}/change-fuel-price", this.testFuelStation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeFuelPriceRequest))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FuelStationResponse fuelStationResponse = objectMapper.readValue(response, FuelStationResponse.class);
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

    @Test()
    @DisplayName("Should get fuel station by id")
    @WithMockAdminUser
    public void shouldGetFuelStationById() throws Exception {
        String response = this.mockMvc.perform(get("/api/fuel-stations/{id}", testFuelStation.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FuelStationResponse fuelStationResponse = objectMapper.readValue(response, FuelStationResponse.class);
        assertThat(fuelStationResponse.getId()).isEqualTo(testFuelStation.getId());
    }

    @Test
    @DisplayName("Should get all fuel stations")
    @WithMockAdminUser
    public void shouldGetAllFuelStations() throws Exception {
        String response = this.mockMvc.perform(get("/api/fuel-stations/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<?> fuelStationResponses = objectMapper.readValue(response, List.class);

        assertThat(fuelStationResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should get all managers assigned to the fuel station")
    @WithMockAdminUser
    public void shouldGetAllManagersAssignedToFuelStation() throws Exception {
        AssignManagerRequest assignManagerRequest = new AssignManagerRequest(testManager.getId());
        this.mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignManagerRequest)))
                .andExpect(status().isOk());

        String response = this.mockMvc
                .perform(get("/api/fuel-stations/{id}/managers", this.testFuelStation.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<?> managers = objectMapper.readValue(response, List.class);
        assertThat(managers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should get all fuel orders related to the fuel station")
    @WithMockAdminUser
    public void shouldGetAllFuelOrdersRelatedToFuelStation() throws Exception {
        CreateFuelOrderRequest createFuelOrderRequest = new CreateFuelOrderRequest(testFuelStation.getId(), FuelGrade.RON_92, 10);
        this.mockMvc.perform(post("/api/fuel-orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFuelOrderRequest)))
                .andExpect(status().isCreated());

        CreateFuelOrderRequest createFuelOrderRequest2 = new CreateFuelOrderRequest(testFuelStation.getId(), FuelGrade.DIESEL, 10);
        this.mockMvc.perform(post("/api/fuel-orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFuelOrderRequest2)))
                .andExpect(status().isCreated());

        String response = this.mockMvc
                .perform(get("/api/fuel-stations/{id}/fuel-orders", this.testFuelStation.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<?> fuelOrders = objectMapper.readValue(response, List.class);
        assertThat(fuelOrders.size()).isEqualTo(2);
    }

}