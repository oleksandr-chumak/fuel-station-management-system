package com.fuelstation.managmentapi.fuelstation.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.fuelstation.application.exceptions.FuelStationNotFoundException;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import com.fuelstation.managmentapi.fuelstation.infrastructure.persistence.FuelStationRepository;

@Component
@AllArgsConstructor
public class GetFuelStationById {

    private final FuelStationRepository fuelStationRepository;

    public FuelStation process(long fuelStationId, Credentials credentials) {
        FuelStation fuelStation = fuelStationRepository.findById(fuelStationId)
                .orElseThrow(() -> new FuelStationNotFoundException(fuelStationId));

        if (!hasAccessRights(fuelStation, credentials)) {
            throw new AccessDeniedException(
                    "User does not have access rights to fuel station with id: " + fuelStationId
            );
        }

        return fuelStationRepository.findById(fuelStationId)
            .orElseThrow(() -> new FuelStationNotFoundException(fuelStationId));
    }

    private boolean hasAccessRights(FuelStation fuelStation, Credentials credentials) {
        return switch (credentials.getRole()) {
            case ADMINISTRATOR -> true;
            case MANAGER -> fuelStation.isManagerAssigned(credentials.getCredentialsId());
        };
    }


}
