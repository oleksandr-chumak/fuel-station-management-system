package com.fuelstation.managmentapi.administrator.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AdminInitializer {

    private static final String DEFAULT_ADMIN_EMAIL = "test@test.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "1234";

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private AdministratorService administratorService;

    @PostConstruct
    public void initAdminIfNotExists() {
        boolean adminExists = administratorRepository
            .findByEmail(DEFAULT_ADMIN_EMAIL) 
            .isPresent();

        if (!adminExists) {
            Administrator admin = administratorService.createAdministrator(DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD);
            System.out.println("Default admin created: " + admin.getEmail());
        } else {
            System.out.println("Default admin already exists.");
        }
    }
}
