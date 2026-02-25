package com.fuelstation.managmentapi.authentication.infrastructure;

public interface CredentialsEmailService {
    void sendManagerCredentials(String to, String password);
}
