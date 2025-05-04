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

import com.fuelstation.managmentapi.common.domain.FuelGrade;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class ChangeFuelPriceIntegrationTest {

@Autowired
    private ChangeFuelPrice changeFuelPrice;
    
    @Autowired
    private FuelStationRepository fuelStationRepository;
    
    @Autowired
    private CreateFuelStation createFuelStation;
    
    private FuelStation testFuelStation;
    
    @BeforeEach
    public void setup() {
        testFuelStation = createFuelStation.process("Test", "Test", "Test", "Test", "Test");
    }

    
    @Test
    @DisplayName("Should throw fuel station not found exception")
    public void shouldThrowFuelStationNotFoundException() {
        // Arrange
        long nonExistentFuelStationId = 99999L;
        
        // Act & Assert
        assertThrows(FuelStationNotFoundException.class, () -> {
            changeFuelPrice.process(nonExistentFuelStationId, FuelGrade.DIESEL, 20);
        });
    }

    @Test
    @DisplayName("Should change fuel price")
    public void shouldChangeFuelPrice() {
        FuelGrade fuelGrade = FuelGrade.DIESEL;
        int newPrice = 20;

        
        FuelStation updatedFuelStation = changeFuelPrice.process(
            testFuelStation.getId(), 
            fuelGrade,
            newPrice
        );
        
        // Assert
        assertTrue(updatedFuelStation.getFuelPrices().stream().anyMatch(fp -> fp.fuelGrade() == fuelGrade && fp.pricePerLiter() == newPrice));
        
        // Verify persistence
        FuelStation retrievedFuelStation = fuelStationRepository.findById(testFuelStation.getId()).orElseThrow();
        assertTrue(retrievedFuelStation.getFuelPrices().stream().anyMatch(fp -> fp.fuelGrade() == fuelGrade && fp.pricePerLiter() == newPrice));
    }
}
