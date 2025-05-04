package com.fuelstation.managmentapi.fuelstation.application.usecases;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;
import com.fuelstation.managmentapi.manager.application.usecases.CreateManager;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.exceptions.ManagerNotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase()
@ActiveProfiles("test")
@Transactional
public class UnassignManagerFromFuelStationIntegrationTest {

    @Autowired
    private UnassignManagerFromFuelStation unassignManagerFromFuelStation;

    @Autowired
    private AssignManagerToFuelStation assignManagerToFuelStation;

    @Autowired
    private CreateManager createManager;

    @Autowired
    private CreateFuelStation createFuelStation;

    @Autowired
    private FuelStationRepository fuelStationRepository;

    private FuelStation testFuelStation;
    private Manager testManager;

    @BeforeEach
    public void setup() {
        testFuelStation = createFuelStation.process("Test", "Test", "Test", "Test", "Test");
        testManager = createManager.process("Test", "Manager", "test@example.com");
        assignManagerToFuelStation.process(testFuelStation.getId(), testManager.getId());
    }

    @Test
    @DisplayName("Should unassign manager")
    public void shouldUnassignManager() {
        // Act
        FuelStation updatedFuelStation = unassignManagerFromFuelStation.process(
            testFuelStation.getId(), 
            testManager.getId()
        );

        // Assert
        assertFalse(updatedFuelStation.getAssignedManagersIds().contains(testManager.getId()));

        // Verify persistence
        FuelStation persistedFuelStation = fuelStationRepository.findById(testFuelStation.getId())
            .orElseThrow();
        assertFalse(persistedFuelStation.getAssignedManagersIds().contains(testManager.getId()));
    }

    @Test
    @DisplayName("Should throw FuelStationNotFoundException")
    public void shouldThrowFuelStationNotFoundException() {
        long nonExistentFuelStationId = 99999L;

        assertThrows(FuelStationNotFoundException.class, () -> {
            unassignManagerFromFuelStation.process(nonExistentFuelStationId, testManager.getId());
        });
    }

    @Test
    @DisplayName("Should throw ManagerNotFoundException")
    public void shouldThrowManagerNotFoundException() {
        long nonExistentManagerId = 99999L;

        assertThrows(ManagerNotFoundException.class, () -> {
            unassignManagerFromFuelStation.process(testFuelStation.getId(), nonExistentManagerId);
        });
    }
}