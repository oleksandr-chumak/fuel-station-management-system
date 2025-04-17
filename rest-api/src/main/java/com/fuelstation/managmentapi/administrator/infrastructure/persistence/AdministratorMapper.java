package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsEntity;

import jakarta.persistence.EntityManager;

@Component
public class AdministratorMapper {

    @Autowired
    private EntityManager em;
    
    public Administrator toDomain(AdministratorEntity entity) {
        return new Administrator(
            entity.getId(),
            entity.getCredentials().getId()
        );
    }

    public AdministratorEntity toEntity(Administrator domain) {
        return new AdministratorEntity(
            domain.getId(),
            em.getReference(CredentialsEntity.class, domain.getCredentialsId())
        );
    }
}