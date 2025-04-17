package com.fuelstation.managmentapi.administrator.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.CredentialsService;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Service 
public class DomainAdministratorService implements AdministratorService {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public Administrator createAdministrator(String email, String password) {
        Credentials credentials = credentialsService.createCredentials(email, password, UserRole.Administrator);
        return administratorRepository.save(new Administrator(null, credentials.getId()));
    }
}
