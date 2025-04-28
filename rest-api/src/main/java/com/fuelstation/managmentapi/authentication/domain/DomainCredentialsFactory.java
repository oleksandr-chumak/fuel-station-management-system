package com.fuelstation.managmentapi.authentication.domain;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DomainCredentialsFactory implements CredentialsFactory{

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Credentials create(String email, String password, UserRole userRole) {
        Optional<Credentials> foundCredentials = credentialsRepository.findByEmailAndRole(email, userRole);
        // TODO Conflict exception should be sent
        if(foundCredentials.isPresent()) {
            throw new IllegalArgumentException("User with email:" + email + "and role:" + userRole.toString() + "already exist");
        }

        return new Credentials(null, null, email, userRole, passwordEncoder.encode(password));
    }
}
