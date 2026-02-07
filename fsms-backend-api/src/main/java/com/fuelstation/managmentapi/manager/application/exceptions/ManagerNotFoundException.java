package com.fuelstation.managmentapi.manager.application.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ManagerNotFoundException extends RuntimeException {

    public ManagerNotFoundException(long managerId) {
        super("Manager with ID " + managerId + " not found");
    }
    
}
