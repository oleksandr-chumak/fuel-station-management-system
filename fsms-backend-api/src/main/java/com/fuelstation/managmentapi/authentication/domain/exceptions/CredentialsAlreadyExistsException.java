package com.fuelstation.managmentapi.authentication.domain.exceptions;

import com.fuelstation.managmentapi.authentication.domain.UserRole;

public class CredentialsAlreadyExistsException extends CredentialsDomainException {

    public CredentialsAlreadyExistsException(String email, UserRole role) {
        super(
            "User with email: " + email + " and role: " + role.toString() + " already exists.",
            "ALREADY_EXISTS"
        );
    }
}
