package com.fuelstation.managmentapi.fuelstation.application.rest;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuelstation.managmentapi.administrator.application.usecases.CreateAdministrator;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.security.AuthenticationService;
import com.fuelstation.managmentapi.fuelstation.application.usecases.AssignManagerToFuelStation;
import com.fuelstation.managmentapi.fuelstation.application.usecases.CreateFuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.manager.application.usecases.CreateManager;
import com.fuelstation.managmentapi.manager.domain.Manager;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class FuelStationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CreateAdministrator createAdministrator;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private CreateFuelStation createFuelStation;
    
    @Autowired
    private CreateManager createManager;
    
    @Autowired
    private AssignManagerToFuelStation assignManagerToFuelStation;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private String testAdminAccessToken;
    private String testManagerAccessToken;
    private FuelStation testFuelStation;
    private Manager testManager;

    @BeforeEach
    public void setup() {
        String adminEmail = "test-admin@test.com";
        String managerEmail = "test-manager@test.com";
        String password = "1234";

        createAdministrator.process(adminEmail, password);
        testManager = createManager.process("Test", "Manager", managerEmail, password);

        testAdminAccessToken = authenticationService.authenticate(adminEmail, password, UserRole.ADMINISTRATOR);
        testManagerAccessToken = authenticationService.authenticate(managerEmail, password, UserRole.MANAGER);
        
        testFuelStation = createFuelStation.process(
            "Test Street", 
            "123", 
            "Test City", 
            "12345", 
            "Test Country"
        );
    }

    @Nested
    class AssignManagerToFuelStationTests {
        @Test
        @DisplayName("Should return 400 when manager id is invalid")
        public void shouldReturn400WhenManagerIdIsInvalid() throws Exception {
            // Given
            Map<String, String> request = new HashMap<>();
            request.put("managerId", "string");
            
            // When & Then
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                .header("Authorization", "Bearer " + testAdminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when fuel station id is invalid")
        public void shouldReturn400WhenFuelStationIdIsInvalid() throws Exception {
            // Given
            AssignManagerRequest request = new AssignManagerRequest();
            request.setManagerId(testManager.getId());
            
            // When & Then
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", "string")
                .header("Authorization", "Bearer " + testAdminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 401 when accessToken is not provided or expired")
        public void shouldReturn401WhenAccessTokenIsNotProvidedOrExpired() throws Exception {
            // Given
            AssignManagerRequest request = new AssignManagerRequest();
            request.setManagerId(testManager.getId());
            
            // When & Then
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                // No Authorization header
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
            
            // With invalid token
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
        }
        
        @Test
        @DisplayName("Should return 403 when manager try to assign manager")
        public void shouldReturn403WhenManagerTryToAssignManager() throws Exception {
            // Given
            AssignManagerRequest request = new AssignManagerRequest();
            request.setManagerId(testManager.getId());
            
            // When & Then
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                .header("Authorization", "Bearer " + testManagerAccessToken) // Manager token
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 404 when fuel station not found")
        public void shouldReturn404WhenFuelStationNotFound() throws Exception {
            // Given
            AssignManagerRequest request = new AssignManagerRequest();
            request.setManagerId(testManager.getId());
            long nonExistentFuelStationId = 9999L;
            
            // When & Then
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", nonExistentFuelStationId)
                .header("Authorization", "Bearer " + testAdminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 404 when manager not found")
        public void shouldReturn404WhenManagerNotFound() throws Exception {
            // Given
            AssignManagerRequest request = new AssignManagerRequest();
            request.setManagerId(9999L); // Non-existent manager ID
            
            // When & Then
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                .header("Authorization", "Bearer " + testAdminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 409 when manager is already assigned")
        public void shouldReturn409WhenManagerIsAlreadyAssigned() throws Exception {
            // Given
            AssignManagerRequest request = new AssignManagerRequest();
            request.setManagerId(testManager.getId());
            
            // First assign the manager
            assignManagerToFuelStation.process(testFuelStation.getId(), testManager.getId());
            
            // When & Then - try to assign again
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                .header("Authorization", "Bearer " + testAdminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Should return 200 and updated fuel station when manager successfully assigned")
        public void shouldReturn200AndUpdatedFuelStationWhenManagerSuccessfullyAssigned() throws Exception {
            // Given
            AssignManagerRequest request = new AssignManagerRequest();
            request.setManagerId(testManager.getId());
            
            // When & Then
            mockMvc.perform(put("/api/fuel-stations/{id}/assign-manager", testFuelStation.getId())
                .header("Authorization", "Bearer " + testAdminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testFuelStation.getId()))
                .andExpect(jsonPath("$.assignedManagersIds", hasItem(testManager.getId().intValue())))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
        }
    }
    
}