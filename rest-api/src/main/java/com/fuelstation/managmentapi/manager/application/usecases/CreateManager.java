package com.fuelstation.managmentapi.manager.application.usecases;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.authentication.application.usecases.CreateCredentials;
import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.ManagerFactory;
import com.fuelstation.managmentapi.manager.domain.events.ManagerCreated;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;

@Component
public class CreateManager {
    
    private final CreateCredentials createCredentials;

    private final ManagerRepository managerRepository;

    private final ManagerFactory managerFactory;

    private final DomainEventPublisher domainEventPublisher;

    public CreateManager(CreateCredentials createCredentials, ManagerRepository managerRepository, ManagerFactory managerFactory, DomainEventPublisher domainEventPublisher) {
        this.createCredentials = createCredentials;
        this.managerRepository = managerRepository;
        this.managerFactory = managerFactory;
        this.domainEventPublisher = domainEventPublisher;
    }

    // TODO if manager creation fails delete credentials
    public Manager process(String firstName, String lastName, String email) {
        return process(firstName, lastName, email, generateRandomPassword(10));
    }

    public Manager process(String firstName, String lastName, String email, String password) {
        Credentials credentials = createCredentials.process(email, password, UserRole.MANAGER);
        Manager createdManager = managerFactory.create(firstName, lastName, credentials.getCredentialsId()); 
        Manager savedManager = managerRepository.save(createdManager);
        domainEventPublisher.publish(new ManagerCreated(savedManager.getId()));
        return savedManager;
    }

    // TODO Move it to credentials
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUV456WXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}