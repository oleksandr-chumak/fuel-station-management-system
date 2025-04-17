package com.fuelstation.managmentapi.authentication.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainCredentialsService implements CredentialsService {

    @Autowired
    private CredentialsFactory credentialsFactory;
    
    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public Credentials createCredentials(String email, String password, UserRole role) {
        Credentials credentials = credentialsFactory.create(email, password, role);
        Credentials savedCredentials = credentialsRepository.save(credentials); 
        return savedCredentials; 
    }
}
