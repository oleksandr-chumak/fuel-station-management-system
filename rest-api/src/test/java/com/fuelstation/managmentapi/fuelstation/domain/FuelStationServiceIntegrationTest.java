package com.fuelstation.managmentapi.fuelstation.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.CredentialsRepository;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelPrice;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.ManagerRepository;
import com.fuelstation.managmentapi.manager.domain.ManagerStatus;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class FuelStationServiceIntegrationTest {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private FuelStationService fuelStationService;
    
    @Autowired
    private FuelStationRepository fuelStationRepository;
    
    @Autowired
    private ManagerRepository managerRepository;
    
    private Manager testManager;
    private FuelStation testFuelStation;

    @BeforeEach
    void setUp() {
        // Clear previous test data
        fuelStationRepository.deleteAll();
       
        // create credentials for a test manger
        
        Credentials credentials = credentialsRepository.save(new Credentials(null,"test@email.com",UserRole.Administrator, "test"));

        // Create a test manager
        testManager = new Manager(null, "Test First Name", "John Doe", ManagerStatus.Active, credentials.getEmail(), credentials.getId());
        testManager = managerRepository.save(testManager);
        
        // Create a test fuel station through the factory
        testFuelStation = fuelStationService.createFuelStation(
            "Main Street", "123", "New York", "10001", "USA"
        );
    }

    @Test
    void testCreateFuelStation() {
        // Assert the test station was created correctly
        assertNotNull(testFuelStation);
        assertNotNull(testFuelStation.getId());
        assertEquals("Main Street", testFuelStation.getAddress().street());
        assertEquals("123", testFuelStation.getAddress().buildingNumber());
        assertEquals("New York", testFuelStation.getAddress().city());
        assertEquals("10001", testFuelStation.getAddress().postalCode());
        assertEquals("USA", testFuelStation.getAddress().country());
        assertEquals(FuelStationStatus.Active, testFuelStation.getStatus());
        
        // Verify it's in the repository
        Optional<FuelStation> foundStation = fuelStationRepository.findById(testFuelStation.getId());
        assertTrue(foundStation.isPresent());
    }

    @Test
    void testAssignManager() {
        // Act
        FuelStation updatedStation = fuelStationService.assignManager(
            testFuelStation.getId(), testManager.getId()
        );
        
        // Assert
        assertNotNull(updatedStation);
        assertTrue(updatedStation.getAssignedManagersIds().contains(testManager.getId()));
        
        // Verify through repository
        Optional<FuelStation> foundStation = fuelStationRepository.findById(testFuelStation.getId());
        assertTrue(foundStation.isPresent());
        assertTrue(foundStation.get().getAssignedManagersIds().contains(testManager.getId()));
    }

    @Test
    void testUnassignManager() {
        // First assign the manager
        FuelStation stationWithManager = fuelStationService.assignManager(
            testFuelStation.getId(), testManager.getId()
        );
        assertTrue(stationWithManager.getAssignedManagersIds().contains(testManager.getId()));
        
        // Act - unassign the manager
        FuelStation updatedStation = fuelStationService.unassignManager(
            testFuelStation.getId(), testManager.getId()
        );
        
        // Assert
        assertNotNull(updatedStation);
        assertFalse(updatedStation.getAssignedManagersIds().contains(testManager.getId()));
        
        // Verify through repository
        Optional<FuelStation> foundStation = fuelStationRepository.findById(testFuelStation.getId());
        assertTrue(foundStation.isPresent());
        assertFalse(foundStation.get().getAssignedManagersIds().contains(testManager.getId()));
    }

    @Test
    void testChangeFuelPrice() {
        // Arrange
        FuelGrade fuelGrade = FuelGrade.RON_95;
        float initialPrice = testFuelStation.getFuelPrices().stream()
                .filter(fp -> fp.fuelGrade() == fuelGrade)
                .findFirst()
                .map(FuelPrice::pricePerLiter)
                .orElse(0.0f);
        float newPrice = initialPrice + 1.0f;  // Increase by 1
        
        // Act
        FuelStation updatedStation = fuelStationService.changeFuelPrice(
            testFuelStation.getId(), fuelGrade, newPrice
        );
        
        // Assert
        assertNotNull(updatedStation);
        float updatedPrice = updatedStation.getFuelPrices().stream()
                .filter(fp -> fp.fuelGrade() == fuelGrade)
                .findFirst()
                .map(FuelPrice::pricePerLiter)
                .orElse(0.0f);
        assertEquals(newPrice, updatedPrice);
        
        // Verify through repository
        Optional<FuelStation> foundStation = fuelStationRepository.findById(testFuelStation.getId());
        assertTrue(foundStation.isPresent());
        float repositoryPrice = foundStation.get().getFuelPrices().stream()
                .filter(fp -> fp.fuelGrade() == fuelGrade)
                .findFirst()
                .map(FuelPrice::pricePerLiter)
                .orElse(0.0f);
        assertEquals(newPrice, repositoryPrice);
    }

    @Test
    void testDeactivateFuelStation() {
        // First assign a manager
        FuelStation stationWithManager = fuelStationService.assignManager(
            testFuelStation.getId(), testManager.getId()
        );
        assertTrue(stationWithManager.getAssignedManagersIds().contains(testManager.getId()));
        
        // Act
        FuelStation deactivatedStation = fuelStationService.deactivateFuelStation(
            testFuelStation.getId()
        );
        
        // Assert
        assertNotNull(deactivatedStation);
        assertEquals(FuelStationStatus.Deactivated, deactivatedStation.getStatus());
        assertTrue(deactivatedStation.getAssignedManagersIds().isEmpty());
        
        // Verify through repository
        Optional<FuelStation> foundStation = fuelStationRepository.findById(testFuelStation.getId());
        assertTrue(foundStation.isPresent());
        assertEquals(FuelStationStatus.Deactivated, foundStation.get().getStatus());
        assertTrue(foundStation.get().getAssignedManagersIds().isEmpty());
    }
    
    @Test
    void testStationNotFound() {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            fuelStationService.deactivateFuelStation(999999L);
        });
        
        assertThrows(NoSuchElementException.class, () -> {
            fuelStationService.assignManager(999999L, testManager.getId());
        });
        
        assertThrows(NoSuchElementException.class, () -> {
            fuelStationService.changeFuelPrice(999999L, FuelGrade.RON_92, 5.0f);
        });
    }
    
    @Test
    void testManagerNotFound() {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            fuelStationService.assignManager(testFuelStation.getId(), 999999L);
        });
        
        assertThrows(NoSuchElementException.class, () -> {
            fuelStationService.unassignManager(testFuelStation.getId(), 999999L);
        });
    }
}
