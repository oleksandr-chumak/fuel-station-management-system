package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.manager.domain.Manager;

@Component
public class ManagerMapper {

    public Manager toDomain(ManagerEntity entity) {
        return new Manager(
            entity.getCredentialsId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getStatus()
        );
    }

    public ManagerEntity toEntity(Manager domain) {
        return new ManagerEntity(
            domain.getCredentialsId(),
            domain.getFirstName(),
            domain.getLastName(),
            domain.getStatus()
        );
    }
}