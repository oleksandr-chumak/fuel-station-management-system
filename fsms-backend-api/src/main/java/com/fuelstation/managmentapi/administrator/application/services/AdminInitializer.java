package com.fuelstation.managmentapi.administrator.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.administrator.application.usecases.CreateAdministrator;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;

import jakarta.annotation.PostConstruct;

@Component
public class AdminInitializer {

    private static final String DEFAULT_ADMIN_EMAIL = "test@test.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "123456789";

    @Autowired
    private CreateAdministrator createAdministrator;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @PostConstruct
    public void initAdminIfNotExists() {
        boolean adminExists = credentialsRepository.findByEmailAndRole(DEFAULT_ADMIN_EMAIL, UserRole.ADMINISTRATOR)
            .isPresent();

        if (!adminExists) {
            createAdministrator.process(DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD);
            System.out.println("Default admin created: " + DEFAULT_ADMIN_EMAIL);
        } else {
            System.out.println("Default admin already exists.");
        }
    }
}
