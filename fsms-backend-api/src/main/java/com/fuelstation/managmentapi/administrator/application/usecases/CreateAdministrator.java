package com.fuelstation.managmentapi.administrator.application.usecases;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.administrator.infrastructure.persistence.AdministratorRepository;
import com.fuelstation.managmentapi.authentication.application.usecases.CreateCredentials;
import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Component
public class CreateAdministrator {

    private final CreateCredentials createCredentials;

    private final AdministratorRepository administratorRepository;

    public CreateAdministrator(CreateCredentials createCredentials, AdministratorRepository administratorRepository) {
        this.createCredentials = createCredentials;
        this.administratorRepository = administratorRepository;
    }

    @Transactional
    public Administrator process(String email, String password) {
        Credentials credentials = createCredentials.process(email, password, UserRole.ADMINISTRATOR);
        return administratorRepository.save(new Administrator(credentials.getCredentialsId()));
    }
    
}
