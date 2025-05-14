package com.fuelstation.managmentapi.fuelstation.application.usecases;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase()
@ActiveProfiles("test")
@Transactional
public class CreateFuelStationIntegrationTest {

    @Autowired
    private CreateFuelStation createFuelStation;

    @Autowired
    private FuelStationRepository fuelStationRepository;

    @Test
    @DisplayName("Should create fuel station")
    public void shouldCreateFuelStation() {
        FuelStation fuelStation = createFuelStation.process("", "", "", "", "");
        Optional<FuelStation> retrievedFuelStation = fuelStationRepository.findById(fuelStation.getId());
        assertTrue(retrievedFuelStation.isPresent());
    }
    
}
