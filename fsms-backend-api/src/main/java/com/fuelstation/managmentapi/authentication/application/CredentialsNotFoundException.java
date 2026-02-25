package com.fuelstation.managmentapi.authentication.application;

import com.fuelstation.managmentapi.authentication.domain.exceptions.CredentialsDomainException;
import org.springframework.http.HttpStatus;

public class CredentialsNotFoundException extends CredentialsDomainException {

    public CredentialsNotFoundException(long credentialsId) {
        super("Credentials with id: %d does not exist".formatted(credentialsId), HttpStatus.NOT_FOUND, "NOT_FOUND");
    }

}
