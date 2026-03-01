package com.fuelstation.managmentapi.authentication.application;

import com.fuelstation.managmentapi.authentication.domain.exceptions.UserDomainException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserDomainException {

    public UserNotFoundException(long userId) {
        super("User with id: %d does not exist".formatted(userId), HttpStatus.NOT_FOUND, "NOT_FOUND");
    }

}
