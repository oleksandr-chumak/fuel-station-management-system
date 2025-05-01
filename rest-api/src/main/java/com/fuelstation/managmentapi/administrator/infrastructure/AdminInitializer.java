package com.fuelstation.managmentapi.administrator.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.administrator.application.usecases.CreateAdministrator;
import com.fuelstation.managmentapi.administrator.infrastructure.persistence.AdministratorRepository;

import jakarta.annotation.PostConstruct;

@Component
public class AdminInitializer {

    private static final String DEFAULT_ADMIN_EMAIL = "test@test.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "1234";

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private CreateAdministrator createAdministrator;

    @PostConstruct
    public void initAdminIfNotExists() {
        boolean adminExists = administratorRepository
            .findByEmail(DEFAULT_ADMIN_EMAIL) 
            .isPresent();

        if (!adminExists) {
            createAdministrator.process(DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD);
            System.out.println("Default admin created: " + DEFAULT_ADMIN_EMAIL);
        } else {
            System.out.println("Default admin already exists.");
        }
    }
}
