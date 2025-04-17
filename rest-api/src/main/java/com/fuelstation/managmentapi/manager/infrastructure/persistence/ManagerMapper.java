package com.fuelstation.managmentapi.manager.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsEntity;
import com.fuelstation.managmentapi.manager.domain.Manager;

import jakarta.persistence.EntityManager;

@Component
public class ManagerMapper {

    @Autowired
    private EntityManager em;
    /**
     * Converts a ManagerEntity to the domain Manager model
     * 
     * @param entity The ManagerEntity to convert
     * @return Manager domain object
     */
    public Manager toDomain(ManagerEntity entity) {
        return new Manager(
            entity.getId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getStatus(),
            entity.getCredentials().getId() 
        );
    }

    /**
     * Converts a domain Manager to ManagerEntity
     * 
     * @param domain The Manager domain object to convert
     * @return ManagerEntity
     */
    public ManagerEntity toEntity(Manager domain) {
        return new ManagerEntity(
            domain.getId(),
            domain.getFirstName(),
            domain.getLastName(),
            domain.getStatus(),
            null,
            em.getReference(CredentialsEntity.class, domain.getCredentialsId())
        );
    }
}