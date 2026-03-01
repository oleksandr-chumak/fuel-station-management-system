package com.fuelstation.managmentapi.administrator.application.services;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.AllArgsConstructor;

import com.fuelstation.managmentapi.administrator.application.usecases.CreateAdministrator;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.UserRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminInitializer {

    private static final String DEFAULT_ADMIN_EMAIL = "test@test.com";
    private static final String DEFAULT_ADMIN_FIRST_NAME = "Oleksandr";
    private static final String DEFAULT_ADMIN_LAST_NAME = "Chumak";
    private static final String DEFAULT_ADMIN_PASSWORD = "123456789";

    private CreateAdministrator createAdministrator;
    private UserRepository userRepository;

    @PostConstruct
    public void initAdminIfNotExists() {
        boolean adminExists = userRepository.findByEmailAndRole(DEFAULT_ADMIN_EMAIL, UserRole.ADMINISTRATOR)
            .isPresent();

        if (!adminExists) {
            createAdministrator.process(
                    DEFAULT_ADMIN_EMAIL,
                    DEFAULT_ADMIN_FIRST_NAME,
                    DEFAULT_ADMIN_LAST_NAME,
                    DEFAULT_ADMIN_PASSWORD,
                    Actor.system()
            );
            System.out.println("Default admin created: " + DEFAULT_ADMIN_EMAIL);
        } else {
            System.out.println("Default admin already exists.");
        }
    }
}
