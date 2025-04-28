package com.fuelstation.managmentapi.authentication.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;

@Service
public class DomainCredentialsService implements CredentialsService {
    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private CredentialsFactory credentialsFactory;
    
    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public Credentials createCredentials(String email, String password, UserRole role) {
        Credentials credentials = credentialsFactory.create(email, password, role);
        Credentials savedCredentials = credentialsRepository.save(credentials); 
        domainEventPublisher.publish(new CredentialsWasCreated(savedCredentials.getCredentialsId(), email, password, role));
        return savedCredentials; 
    }
}
