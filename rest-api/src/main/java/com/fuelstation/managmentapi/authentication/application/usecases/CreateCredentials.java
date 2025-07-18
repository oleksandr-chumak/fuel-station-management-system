package com.fuelstation.managmentapi.authentication.application.usecases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.CredentialsFactory;
import com.fuelstation.managmentapi.authentication.domain.CredentialsCreated;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.domain.exceptions.CredentialsAlreadyExistsException;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;

@Component
public class CreateCredentials {
    
    private final CredentialsRepository credentialsRepository;

    private final CredentialsFactory credentialsFactory;

    private final DomainEventPublisher domainEventPublisher;

    private final PasswordEncoder passwordEncoder;

    public CreateCredentials(CredentialsRepository credentialsRepository, CredentialsFactory credentialsFactory, DomainEventPublisher domainEventPublisher, PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.credentialsFactory = credentialsFactory;
        this.domainEventPublisher = domainEventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    public Credentials process(String email, String password, UserRole role) {
        Optional<Credentials> foundCredentials = credentialsRepository.findByEmailAndRole(email, role);

        if(foundCredentials.isPresent()) {
            throw new CredentialsAlreadyExistsException(email, role);
        }

        Credentials credentials = credentialsFactory.create(email, passwordEncoder.encode(password), role);
        Credentials savedCredentials = credentialsRepository.save(credentials); 
        domainEventPublisher.publish(new CredentialsCreated(savedCredentials.getCredentialsId(), email, password, role));
        return savedCredentials; 
    }
    
}
