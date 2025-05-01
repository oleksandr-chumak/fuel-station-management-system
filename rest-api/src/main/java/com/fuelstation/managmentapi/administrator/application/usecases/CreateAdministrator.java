package com.fuelstation.managmentapi.administrator.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.administrator.infrastructure.persistence.AdministratorRepository;
import com.fuelstation.managmentapi.authentication.application.usecases.CreateCredentials;
import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Component
public class CreateAdministrator {

    @Autowired
    private CreateCredentials createCredentials;

    @Autowired
    private AdministratorRepository administratorRepository;

    public Administrator process(String email, String password) {
        Credentials credentials = createCredentials.process(email, password, UserRole.ADMINISTRATOR);
        return administratorRepository.save(new Administrator(null, credentials.getCredentialsId()));
    }
    
}
