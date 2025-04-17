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
        if (entity == null) {
            return null;
        }

        Manager manager = new Manager();
        manager.setId(entity.getId());
        manager.setFirstName(entity.getFirstName());
        manager.setLastName(entity.getLastName());
        manager.setStatus(entity.getStatus());
        
        // Set email from credentials
        if (entity.getCredentials() != null) {
            manager.setEmail(entity.getCredentials().getEmail());
            manager.setCredentialsId(entity.getCredentials().getId());
        }

        return manager;
    }

    /**
     * Converts a domain Manager to ManagerEntity
     * 
     * @param domain The Manager domain object to convert
     * @return ManagerEntity
     */
    public ManagerEntity toEntity(Manager domain) {
        if (domain == null) {
            return null;
        }

        ManagerEntity entity = new ManagerEntity();
        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setStatus(domain.getStatus());
        entity.setCredentials(em.getReference(CredentialsEntity.class, domain.getCredentialsId()));

        return entity;
    }
}