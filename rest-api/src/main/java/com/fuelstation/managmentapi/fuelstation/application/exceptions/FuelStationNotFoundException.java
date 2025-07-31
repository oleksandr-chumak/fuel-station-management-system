package com.fuelstation.managmentapi.fuelstation.application.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FuelStationNotFoundException extends RuntimeException {

    public FuelStationNotFoundException(long fuelStationId) {
        super(String.format("Fuel station %d not found", fuelStationId));
    }
    
}
