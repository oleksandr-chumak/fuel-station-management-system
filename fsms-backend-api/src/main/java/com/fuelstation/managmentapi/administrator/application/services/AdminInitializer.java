package com.fuelstation.managmentapi.administrator.application.services;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import lombok.AllArgsConstructor;

import com.fuelstation.managmentapi.administrator.application.usecases.CreateAdministrator;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminInitializer {

    private static final String DEFAULT_ADMIN_EMAIL = "test@test.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "123456789";

    private CreateAdministrator createAdministrator;
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
