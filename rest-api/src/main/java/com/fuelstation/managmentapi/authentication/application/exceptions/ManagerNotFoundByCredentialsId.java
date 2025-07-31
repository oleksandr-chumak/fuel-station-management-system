package com.fuelstation.managmentapi.authentication.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ManagerNotFoundByCredentialsId extends RuntimeException {

    public ManagerNotFoundByCredentialsId() {
        super("Manager not found by credentials id");
    }

}
