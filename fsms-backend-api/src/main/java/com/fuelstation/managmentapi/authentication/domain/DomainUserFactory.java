package com.fuelstation.managmentapi.authentication.domain;

import org.springframework.stereotype.Component;

@Component
public class DomainUserFactory implements UserFactory {

    @Override
    public User create(String email, String firstName, String lastName, String password, UserRole userRole) {
        return new User(
                null,
                firstName,
                lastName,
                firstName + " " + lastName,
                email,
                UserStatus.ACTIVE,
                userRole,
                password
        );
    }

}
