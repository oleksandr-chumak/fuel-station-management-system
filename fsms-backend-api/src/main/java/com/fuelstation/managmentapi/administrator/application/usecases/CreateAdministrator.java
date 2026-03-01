package com.fuelstation.managmentapi.administrator.application.usecases;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.common.domain.Actor;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.administrator.infrastructure.persistence.AdministratorRepository;
import com.fuelstation.managmentapi.authentication.application.usecases.CreateUser;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Component
@AllArgsConstructor
public class CreateAdministrator {

    private final CreateUser createUser;
    private final AdministratorRepository administratorRepository;

    @Transactional
    public Administrator process(String email, String firstName, String lastName, String password, Actor actor) {
        User user = createUser.process(email, firstName, lastName, password, UserRole.ADMINISTRATOR, actor);
        return administratorRepository.save(Administrator.fromUser(user));
    }
    
}
