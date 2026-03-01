package com.fuelstation.managmentapi.authentication.domain.exceptions;

import com.fuelstation.managmentapi.authentication.domain.UserRole;

public class UserAlreadyExistsException extends UserDomainException {

    public UserAlreadyExistsException(String email, UserRole role) {
        super(
            "User with email: " + email + " and role: " + role.toString() + " already exists.",
            "ALREADY_EXISTS"
        );
    }
}
