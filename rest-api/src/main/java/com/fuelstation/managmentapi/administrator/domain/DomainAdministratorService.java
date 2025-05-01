package com.fuelstation.managmentapi.administrator.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.authentication.application.usecases.CreateCredentials;
import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Service 
public class DomainAdministratorService implements AdministratorService {

    @Autowired
    private CreateCredentials createCredentials;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public Administrator createAdministrator(String email, String password) {
        Credentials credentials = createCredentials.process(email, password, UserRole.ADMINISTRATOR);
        return administratorRepository.save(new Administrator(null, credentials.getCredentialsId()));
    }
}
