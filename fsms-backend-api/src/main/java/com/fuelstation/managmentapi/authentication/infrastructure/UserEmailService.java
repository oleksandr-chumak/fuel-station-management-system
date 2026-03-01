package com.fuelstation.managmentapi.authentication.infrastructure;

public interface UserEmailService {
    void sendManagerCredentials(String to, String password);
}
