package com.fuelstation.managmentapi.manager.application.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ManagerNotFoundException extends ManagerDomainException {

    public ManagerNotFoundException(long managerId) {
        super("Manager #%d does not exist".formatted(managerId), HttpStatus.NOT_FOUND, "NOT_FOUND");
    }
    
}
