package com.fuelstation.managmentapi.authentication.application.usecases;


import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.administrator.infrastructure.persistence.AdministratorRepository;
import com.fuelstation.managmentapi.authentication.application.exceptions.AdministratorNotFoundByCredentialsIdException;
import com.fuelstation.managmentapi.authentication.application.exceptions.ManagerNotFoundByCredentialsId;
import com.fuelstation.managmentapi.authentication.application.exceptions.UnsupportedUserRoleException;
import com.fuelstation.managmentapi.authentication.application.exceptions.UserNotFoundException;
import com.fuelstation.managmentapi.authentication.application.Me;
import com.fuelstation.managmentapi.authentication.domain.Credentials;
import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;
import com.fuelstation.managmentapi.manager.domain.Manager;
import com.fuelstation.managmentapi.manager.infrastructure.persistence.ManagerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class GetMe {

    private final CredentialsRepository credentialsRepository;
    private final ManagerRepository managerRepository;
    private final AdministratorRepository administratorRepository;

    public GetMe(CredentialsRepository credentialsRepository, ManagerRepository managerRepository, AdministratorRepository administratorRepository) {
       this.credentialsRepository = credentialsRepository;
       this.managerRepository = managerRepository;
       this.administratorRepository = administratorRepository;
    }

    @Transactional
    public Me process(String username) {
        Credentials credentials = credentialsRepository.findByEmail(username)
                .orElseThrow(UserNotFoundException::new);

        long userId = switch (credentials.getRole()) {
            case UserRole.MANAGER -> {
                Manager manager = managerRepository.findByCredentialsId(credentials.getCredentialsId())
                        .orElseThrow(ManagerNotFoundByCredentialsId::new);
                yield manager.getId();
            }
            case UserRole.ADMINISTRATOR -> {
                Administrator administrator = administratorRepository.findByCredentialsId(credentials.getCredentialsId())
                        .orElseThrow(AdministratorNotFoundByCredentialsIdException::new);
                yield administrator.getId();
            }
            default -> throw new UnsupportedUserRoleException(credentials.getRole());
        };

        return new Me(userId, credentials.getEmail(), credentials.getRole().toString());
    }
}
