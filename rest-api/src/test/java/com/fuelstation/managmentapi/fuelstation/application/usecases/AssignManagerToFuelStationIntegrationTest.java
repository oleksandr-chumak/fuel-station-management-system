package com.fuelstation.managmentapi.fuelstation.application.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.ManagerAlreadyAssignedException;
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
public class AssignManagerToFuelStationIntegrationTest {
    
    @Autowired
    private AssignManagerToFuelStation assignManagerToFuelStation;
    
    @Autowired
    private FuelStationRepository fuelStationRepository;
    
    @Autowired
    private CreateManager createManager;
    
    @Autowired
    private CreateFuelStation createFuelStation;
    
    private FuelStation testFuelStation;
    private Manager testManager;
    
    @BeforeEach
    public void setup() {
        
        testFuelStation = createFuelStation.process("Test", "Test", "Test", "Test", "Test");
        
        testManager = createManager.process(
            "Test", 
            "Manager", 
            "test@example.com" 
        );
    }
    
    @Test
    @DisplayName("Should throw manager not found exception")
    public void shouldThrowManagerNotFoundException() {
        // Arrange
        long nonExistentManagerId = 99999L;
        
        // Act & Assert
        assertThrows(ManagerNotFoundException.class, () -> {
            assignManagerToFuelStation.process(testFuelStation.getId(), nonExistentManagerId);
        });
    }
    
    @Test
    @DisplayName("Should throw fuel station not found exception")
    public void shouldThrowFuelStationNotFoundException() {
        // Arrange
        long nonExistentFuelStationId = 99999L;
        
        // Act & Assert
        assertThrows(FuelStationNotFoundException.class, () -> {
            assignManagerToFuelStation.process(nonExistentFuelStationId, testManager.getId());
        });
    }
    
    @Test
    @DisplayName("Should assign manager")
    public void shouldAssignManager() {
        // Act
        FuelStation updatedFuelStation = assignManagerToFuelStation.process(
            testFuelStation.getId(), 
            testManager.getId()
        );
        
        // Assert
        assertTrue(updatedFuelStation.getAssignedManagersIds().contains(testManager.getId()));
        
        // Verify persistence
        FuelStation retrievedFuelStation = fuelStationRepository.findById(testFuelStation.getId())
            .orElseThrow();
        assertTrue(retrievedFuelStation.getAssignedManagersIds().contains(testManager.getId()));
    }
    
    @Test
    @DisplayName("Should throw manager already assigned exception")
    public void shouldThrowManagerAlreadyAssignedException() {
        // Arrange
        // First assign the manager
        assignManagerToFuelStation.process(testFuelStation.getId(), testManager.getId());
        
        // Act & Assert
        // Try to assign the same manager again
        assertThrows(ManagerAlreadyAssignedException.class, () -> {
            assignManagerToFuelStation.process(testFuelStation.getId(), testManager.getId());
        });
    }
}
