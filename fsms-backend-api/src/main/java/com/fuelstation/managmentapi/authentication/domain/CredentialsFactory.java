package com.fuelstation.managmentapi.authentication.domain;

public interface CredentialsFactory {
   Credentials create(String email, String password, UserRole userRole);
}
