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

import com.fuelstation.managmentapi.fuelstation.application.rest.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationAlreadyDeactivatedException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStationStatus;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase()
@ActiveProfiles("test")
@Transactional
public class DeactivateFuelStationIntegrationTest {

    @Autowired
    private DeactivateFuelStation deactivateFuelStation;

    @Autowired
    private CreateFuelStation createFuelStation;

    @Autowired
    private FuelStationRepository fuelStationRepository;

    private FuelStation testFuelStation;
    
    @BeforeEach
    public void setup() {
        testFuelStation = createFuelStation.process("Test", "Test", "Test", "Test", "Test");
    }

    @Test
    @DisplayName("Should deactivate fuel station")
    public void shouldDeactivateFuelStation() {
        FuelStation updatedFuelStation = deactivateFuelStation.process(testFuelStation.getId());

        assertTrue(updatedFuelStation.getStatus() == FuelStationStatus.DEACTIVATED);
        assertTrue(fuelStationRepository.findById(testFuelStation.getId()).get().getStatus() == FuelStationStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("Should throw fuel station already deactivated exception")
    public void shouldThrowFuelStationAlreadyDeactivatedException() {
        deactivateFuelStation.process(testFuelStation.getId());

        assertThrows(FuelStationAlreadyDeactivatedException.class, () -> {
            deactivateFuelStation.process(testFuelStation.getId());
        });
    }
    
    @Test
    @DisplayName("Should throw fuel station not found exception")
    public void shouldThrowFuelStationNotFoundException() {
        long notExistentFuelStationId = 99999l; 

        assertThrows(FuelStationNotFoundException.class, () -> {
            deactivateFuelStation.process(notExistentFuelStationId);
        });
    }
}
