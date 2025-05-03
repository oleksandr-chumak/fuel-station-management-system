package com.fuelstation.managmentapi.authentication.application;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsResponse {
    private long userId;
    private String email; 
    private UserRole role; 

    public static CredentialsResponse fromDomain(Credentials credentials, long userId) {
        return new CredentialsResponse(
            userId,
            credentials.getEmail(), 
            credentials.getRole()
        );
    }

}
