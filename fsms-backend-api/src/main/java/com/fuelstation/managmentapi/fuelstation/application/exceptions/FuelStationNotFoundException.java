package com.fuelstation.managmentapi.fuelstation.application.exceptions;

import com.fuelstation.managmentapi.fuelstation.domain.exceptions.FuelStationDomainException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FuelStationNotFoundException extends FuelStationDomainException {

    public FuelStationNotFoundException(long fuelStationId) {
        super(
                String.format("Fuel station %d not found", fuelStationId),
                HttpStatus.NOT_FOUND,
                "NOT_FOUND"
        );
    }
    
}
