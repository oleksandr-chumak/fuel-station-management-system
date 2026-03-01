package com.fuelstation.managmentapi.fuelstation.application.support;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.fuelstation.domain.models.FuelStation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;

@Component
@AllArgsConstructor
public class FuelStationAccessControlChecker {

    @SneakyThrows
    public void checkAccess(FuelStation station, User user) {
        boolean hasAccess = switch (user.getRole()) {
            case ADMINISTRATOR -> true;
            case MANAGER -> station.isManagerAssigned(user.getUserId());
        };
        if (!hasAccess) {
            throw new AccessDeniedException("No access to station: " + station.getFuelStationId());
        }
    }

}
