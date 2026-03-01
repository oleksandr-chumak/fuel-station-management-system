package com.fuelstation.managmentapi.manager.application.usecases;

import java.security.SecureRandom;

import com.fuelstation.managmentapi.common.domain.Actor;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import com.fuelstation.managmentapi.authentication.application.usecases.CreateUser;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.domain.events.ManagerCreated;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateManager {
    
    private final CreateUser createUser;
    private final DomainEventPublisher domainEventPublisher;

    // TODO if manager creation fails delete credentials
    @Transactional
    public Manager process(String firstName, String lastName, String email) {
        return process(firstName, lastName, email, generateRandomPassword(10), Actor.system());
    }

    @Transactional
    public Manager process(String firstName, String lastName, String email, String password, Actor actor) {
        var user = createUser.process(email, firstName, lastName, password, UserRole.MANAGER, actor);
        domainEventPublisher.publish(new ManagerCreated(user.getUserId(), actor));
        return Manager.fromUser(user);
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