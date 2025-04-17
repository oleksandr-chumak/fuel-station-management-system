package com.fuelstation.managmentapi.authentication.domain;

public interface CredentialsService {
    public Credentials createCredentials(String email, String password, UserRole role);
}
