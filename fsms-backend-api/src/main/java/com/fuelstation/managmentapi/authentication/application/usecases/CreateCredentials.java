package com.fuelstation.managmentapi.authentication.application.usecases;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.CredentialsFactory;
import com.fuelstation.managmentapi.authentication.domain.CredentialsCreated;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.domain.exceptions.CredentialsAlreadyExistsException;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateCredentials {
    
    private final CredentialsRepository credentialsRepository;
    private final CredentialsFactory credentialsFactory;
    private final DomainEventPublisher domainEventPublisher;
    private final PasswordEncoder passwordEncoder;

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
