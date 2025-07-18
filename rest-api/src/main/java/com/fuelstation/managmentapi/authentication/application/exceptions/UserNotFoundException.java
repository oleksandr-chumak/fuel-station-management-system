package com.fuelstation.managmentapi.authentication.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User does not exist");
    }

}
