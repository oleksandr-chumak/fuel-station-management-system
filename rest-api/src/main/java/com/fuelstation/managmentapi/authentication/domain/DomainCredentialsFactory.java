package com.fuelstation.managmentapi.authentication.domain;

import org.springframework.stereotype.Component;

@Component
public class DomainCredentialsFactory implements CredentialsFactory{

    @Override
    public Credentials create(String email, String password, UserRole userRole) {
        return new Credentials(null, null, email, userRole, password);
    }
}
