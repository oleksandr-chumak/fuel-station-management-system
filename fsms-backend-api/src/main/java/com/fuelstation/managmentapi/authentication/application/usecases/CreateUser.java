package com.fuelstation.managmentapi.authentication.application.usecases;

import java.util.Optional;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.common.domain.Actor;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fuelstation.managmentapi.authentication.domain.UserFactory;
import com.fuelstation.managmentapi.authentication.domain.UserCreated;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.domain.exceptions.UserAlreadyExistsException;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.UserRepository;
import com.fuelstation.managmentapi.common.domain.DomainEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUser {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final DomainEventPublisher domainEventPublisher;
    private final PasswordEncoder passwordEncoder;

    public User process(String email, String firstName, String lastName, String password, UserRole role, Actor performedBy) {
        Optional<User> foundCredentials = userRepository.findByEmailAndRole(email, role);

        if (foundCredentials.isPresent()) {
            throw new UserAlreadyExistsException(email, role);
        }

        var user = userFactory.create(email, firstName, lastName, passwordEncoder.encode(password), role);
        var savedUser = userRepository.save(user);
        var event = new UserCreated(
                savedUser.getUserId(),
                email,
                password,
                role,
                performedBy
        );

        domainEventPublisher.publish(event);
        return savedUser;
    }

}
